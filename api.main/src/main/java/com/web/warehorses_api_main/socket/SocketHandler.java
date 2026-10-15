package com.web.warehorses_api_main.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.web.warehorses_api_main.socket.dto.SocketRequestDto;
import com.web.warehorses_api_main.socket.dto.SocketResponseDto;
import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityRequestDto;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.activity.repository.ActivityRepository;
import com.web.warehorses_secure_persistence.domain.activity.util.mapper.ActivityMapper;
import com.web.warehorses_secure_persistence.domain.client.dto.ClientRequestDto;
import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import com.web.warehorses_secure_persistence.domain.client.repository.ClientRepository;
import com.web.warehorses_secure_persistence.domain.client.util.mapper.ClientMapper;
import com.web.warehorses_secure_persistence.domain.company.dto.CompanyRequestDto;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import com.web.warehorses_secure_persistence.domain.company.repository.CompanyRepository;
import com.web.warehorses_secure_persistence.domain.company.util.mapper.CompanyMapper;
import com.web.warehorses_secure_persistence.domain.item.dto.ItemRequestDto;
import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import com.web.warehorses_secure_persistence.domain.item.repository.ItemRepository;
import com.web.warehorses_secure_persistence.domain.item.util.mapper.ItemMapper;
import com.web.warehorses_secure_persistence.domain.user.dto.UserRegisterRequestDto;
import com.web.warehorses_secure_persistence.domain.user.dto.UserResponseDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.domain.user.repository.UserRepository;
import com.web.warehorses_secure_persistence.domain.user.util.PasswordEncoderProvider;
import com.web.warehorses_secure_persistence.domain.warehouse.dto.WarehouseRequestDto;
import com.web.warehorses_secure_persistence.domain.warehouse.dto.WarehouseResponseDto;
import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import com.web.warehorses_secure_persistence.domain.warehouse.repository.WarehouseRepository;
import com.web.warehorses_secure_persistence.domain.user.util.mapper.UserMapper;
import com.web.warehorses_secure_persistence.exception.CompanyNotFoundException;
import com.web.warehorses_secure_persistence.jwt.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class SocketHandler {

    private final SocketIOServer server;
    private final JwtProvider jwtProvider;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final ItemRepository itemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ActivityMapper activityMapper;
    private final ClientMapper clientMapper;
    private final CompanyMapper companyMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    /** Required {@code GrantedAuthority} as String to match logged-in user
     * and validate permissions and allow access to some event listener code blocks */
    private static final String REQUIRED_AUTHORITY = "ROLE_MANAGER";
    private static final String TOKEN_COOKIE_NAME = "jwt";

    public SocketHandler(SocketIOServer server,
                         JwtProvider jwtProvider,
                         ActivityRepository activityRepository,
                         UserRepository userRepository,
                         ClientRepository clientRepository,
                         CompanyRepository companyRepository,
                         ItemRepository itemRepository,
                         WarehouseRepository warehouseRepository,
                         ActivityMapper activityMapper,
                         ClientMapper clientMapper,
                         CompanyMapper companyMapper,
                         ItemMapper itemMapper,
                         UserMapper userMapper) {
        this.server = server;
        this.jwtProvider = jwtProvider;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.itemRepository =itemRepository;
        this.warehouseRepository = warehouseRepository;
        this.activityMapper = activityMapper;
        this.clientMapper = clientMapper;
        this.companyMapper = companyMapper;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void configure() {
        server.addConnectListener(client -> {
            log.info("Current sessionId: {}", client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            log.info("Client disconnected: {}", client.getSessionId());
        });

        final String AUTHORITY_EXCEPTION_MESSAGE = "Authenticated user has no permission to perform this action";

        final String ACTIVITY_CREATE_EVENT;
        ACTIVITY_CREATE_EVENT = "activity:create";
        server.addEventListener(
                ACTIVITY_CREATE_EVENT,
                SocketRequestDto.class,
                (socketIOClient, data, ackSender) -> {

                    log.info(
                            "Data send in activity:create event: "
                                    .concat(((ActivityRequestDto) data.dto()).type().name()));
                    final Activity created = activityRepository.save(activityMapper.toEntity((ActivityRequestDto) data.dto()));
                    // Broadcast the DTO, never the entity: serialising a saved
                    // Activity would walk Activity.client -> Client.activities
                    // -> Activity and fail on the cycle / lazy proxies.
                    server.getBroadcastOperations().sendEvent(
                            ACTIVITY_CREATE_EVENT,
                            activityMapper.toDto(created)
                    );
                }
        );

        final String CLIENT_CREATE_EVENT = "client:create";
        server.addEventListener(
                CLIENT_CREATE_EVENT,
                ClientRequestDto.class,
                (socketIOClient, data, ackSender) -> {

                    try {
                        Client client = new Client();
                        client.setName(data.name());
                        client.setPhoneNumber(data.phoneNumber());
                        client = clientRepository.save(client);
                        server.getBroadcastOperations()
                                .sendEvent(
                                        CLIENT_CREATE_EVENT,
                                        clientMapper.toDto(client));
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", true, "data", client));
                        }
                    } catch (Exception e) {
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", false, "message", e.getMessage()));
                        }
                    }
                }
        );

        final String COMPANY_CREATE_EVENT = "company:create";
        server.addEventListener(
                COMPANY_CREATE_EVENT,
                SocketRequestDto.class,
                (socketIOClient, data, ackSender) -> {
                    if (!loggedIntoUserIsManager(getCurrentToken(socketIOClient))) {
                        throw new SocketException(AUTHORITY_EXCEPTION_MESSAGE);
                    }
                    try {
                        Company company = companyMapper.toEntity((CompanyRequestDto) data.dto());
                        company = companyRepository.save(company);

                        final SocketResponseDto responseDto = new SocketResponseDto(
                                socketIOClient.getSessionId(),
                                companyMapper.toDto(company)
                        );
                        server.getBroadcastOperations()
                                .sendEvent(
                                        COMPANY_CREATE_EVENT.concat("d"),
                                        responseDto);
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", true, "data", company));
                        }
                    } catch (Exception e) {
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", false, "message", e.getMessage()));
                        }
                    }
                }
        );

        final String ITEM_CREATE_EVENT = "item:create";
        server.addEventListener(
                ITEM_CREATE_EVENT,
                SocketRequestDto.class,
                (socketIOClient, data, ackSender) -> {
                    if (!loggedIntoUserIsManager(getCurrentToken(socketIOClient))) {
                        throw new AccessDeniedException(AUTHORITY_EXCEPTION_MESSAGE);
                    }
                    try {
                        ItemRequestDto itemRequestDto = (ItemRequestDto) data.dto();
                        Item item = new Item();
                        item.setDescription(itemRequestDto.description());
                        item.setSupplier(itemRequestDto.supplier());
                        item.setUnitPrice(itemRequestDto.unitPrice());
                        item.setSalePrice(itemRequestDto.salePrice());
                        item.setQuantity(itemRequestDto.quantity());
                        item =itemRepository.save(item);

                        final SocketResponseDto responseDto = new SocketResponseDto(
                                socketIOClient.getSessionId(),
                                item
                        );
                        server.getBroadcastOperations()
                                .sendEvent(
                                        ITEM_CREATE_EVENT,
                                        responseDto
                                );
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", true, "data", item));
                        }
                    } catch (Exception e) {
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", false, "message", e.getMessage()));
                        }
                    }
                }
        );

        final String USER_CREATE_EVENT = "user:create";
        server.addEventListener(
                USER_CREATE_EVENT,
                UserRegisterRequestDto.class,
                (socketIOClient, data, ackSender) -> {
                    final String token = getCurrentToken(socketIOClient);
                    log.info(
                            "Current sessionId: {}; User creation JWT: {}",
                            socketIOClient.getSessionId(),
                            token);
                    if (!loggedIntoUserIsManager(token)) {
                        server.getBroadcastOperations().sendEvent(
                                USER_CREATE_EVENT,
                                false
                        );
                        throw new AccessDeniedException(AUTHORITY_EXCEPTION_MESSAGE);
                    }
                    try {
                        final UserResponseDto createdUserDto = createUser(data);
                        server.getBroadcastOperations().sendEvent(
                                USER_CREATE_EVENT,
                                createdUserDto
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        final String WAREHOUSE_CREATE_EVENT = "warehouse:create";
        server.addEventListener(
                WAREHOUSE_CREATE_EVENT,
                WarehouseRequestDto.class,
                (socketIOClient, data, ackSender) -> {
                    if (!loggedIntoUserIsManager(getCurrentToken(socketIOClient))) {
                        throw new AccessDeniedException(AUTHORITY_EXCEPTION_MESSAGE);
                    }
                    try {
                        Warehouse warehouse = new Warehouse();
                        warehouse.setAddress(data.address());
                        // Resolve the entity from the NIT carried by the payload
                        // instead of trusting a client-supplied Company object.
                        warehouse
                                .setCompany(
                                        companyRepository
                                                .findByNit(data.companyNit())
                                                .orElseThrow(CompanyNotFoundException::new));
                        warehouse = warehouseRepository.save(warehouse);
                        final WarehouseResponseDto warehouseResponseDto =
                                WarehouseResponseDto.builder()
                                        .warehouseId(warehouse.getWarehouseId())
                                        .address(warehouse.getAddress())
                                        .company(companyMapper.toDto(warehouse.getCompany()))
                                        .items(
                                                warehouse.getItems()
                                                        .stream()
                                                        .map(itemMapper::toDto)
                                                        .toList()
                                        )
                                        .build();
                        server.getBroadcastOperations().sendEvent(WAREHOUSE_CREATE_EVENT, warehouseResponseDto);
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", true, "data", warehouse));
                        }
                    } catch (Exception e) {
                        if (ackSender.isAckRequested()) {
                            ackSender.sendAckData(Map.of("success", false, "message", e.getMessage()));
                        }
                    }
                }
        );
    }

    /**
     * Inserts in db a User object that must to integrate a company (mandatory)
     * @return new user response dto mapped from entity
     */
    @Transactional
    private UserResponseDto createUser(UserRegisterRequestDto dto) {
        // findByIdCardNumber THROWS UserNotFoundException when the row is absent,
        // which is exactly the normal case during registration -- so using it as
        // an existence probe made every create fail with that exception.
        if (userRepository.existsByIdCardNumber(dto.idCardNumber())) {
            throw new IllegalArgumentException(
                    "User with id card number # ".concat(dto.idCardNumber()) + " already exists");
        }
        User user = new User(
                dto.idCardNumber(),
                getCompanyByNit(dto.companyNit()),
                PasswordEncoderProvider.encodePassword(dto.password()),
                dto.fullname());
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    private Company getCompanyByNit(String nit) {
        return companyRepository
                .findByNit(nit).orElseThrow(CompanyNotFoundException::new);
    }

    /**
     * Checks whether the token's "authorities" claim contains the required
     * authority.
     *
     * <p>The claim is written from Spring Security's GrantedAuthority objects,
     * so after a JSON round trip it comes back in whichever of these shapes
     * Jackson produced. All are handled:
     * <ul>
     *   <li>[{"authority":"ROLE_MANAGER"}]  -- SimpleGrantedAuthority as an object</li>
     *   <li>["ROLE_MANAGER"]               -- plain strings</li>
     *   <li>"ROLE_MANAGER,ROLE_USER"       -- single comma/space separated string</li>
     * </ul>
     * Comparing case-insensitively because the ROLE_ prefix is what matters,
     * not the casing the issuer happened to use.
     */
    private boolean hasAuthority(Claims claims, String required) {
        final Object raw = claims.get("authorities");
        if (raw instanceof java.util.Collection<?> collection) {
            for (Object item : collection) {
                if (item instanceof java.util.Map<?, ?> map) {
                    Object value = map.get("authority");
                    if (value != null && required.equalsIgnoreCase(value.toString().trim())) {
                        return true;
                    }
                } else if (item != null &&
                        required.equalsIgnoreCase(item.toString().trim())) {
                    return true;
                }
            }
            return false;
        }
        if (raw instanceof String text) {
            for (String part : text.split("[,\\s]+")) {
                if (required.equalsIgnoreCase(part.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resolves the JWT for THIS connection.
     *
     * <p>One source is tried here: <b>jwt cookie on the handshake</b>
     *      <b>of {@code socketIOClient}</b> param.
     *      Works when the socket connection
     *      is same-origin (e.g. behind the Vite dev proxy, or deployed behind
     *      one domain), because then no CORS check applies and the browser
     *      sends cookies normally.
     *
     * <p>The source actually used is logged
     */
    public String getCurrentToken(SocketIOClient socketIOClient) {
        if (socketIOClient == null || socketIOClient.getHandshakeData() == null) {
            return null;
        }
        final HandshakeData handshake = socketIOClient.getHandshakeData();
        String token;

        // cookie -- only populated for same-origin connections
        final String cookieHeader = handshake.getHttpHeaders().get("Cookie");
        if (cookieHeader != null && !cookieHeader.isBlank()) {
            final Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieHeader);
            token = cookies.stream()
                    .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.name()))
                    .map(Cookie::value)
                    .filter(SocketHandler::isUsable)
                    .findFirst()
                    .orElse(null);
            if (isUsable(token)) {
                log.debug("Socket token resolved from cookie for session={}", socketIOClient.getSessionId());
                return stripBearer(token);
            }
        }

        return null;
    }

    private static boolean isUsable(String value) {
        return value != null && !value.isBlank() && !"null".equalsIgnoreCase(value.trim());
    }

    private static String stripBearer(String value) {
        final String trimmed = value.trim();
        return trimmed.regionMatches(true, 0, "Bearer ", 0, 7)
                ? trimmed.substring(7).trim()
                : trimmed;
    }

    /**
     * The one definition of "this connection may act as a manager".
     *
     * <p>Delegates to the same authorities-claim check the auth:token listener
     * uses, so the gate applied when caching and the gate applied when acting
     * can never drift apart. The previous version compared the "role" claim and
     * "isAccountNonLocked", which no longer matched how the token is minted.
     */
    private boolean loggedIntoUserIsManager(String token) {
        if (token == null || !jwtProvider.validateToken(token)) {
            return false;
        }
        return hasAuthority(jwtProvider.getClaims(token), REQUIRED_AUTHORITY);
    }
}