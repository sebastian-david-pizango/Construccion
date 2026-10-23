package com.collectiverse.collectiverse.proceso;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.collectiverse.collectiverse.cu.publication.response.ResponsePublication;
import com.collectiverse.collectiverse.dominio.entity.Category;
import com.collectiverse.collectiverse.dominio.entity.Product;
import com.collectiverse.collectiverse.dominio.repository.RepoCategory;
import com.collectiverse.collectiverse.dominio.repository.RepoProduct;

/**
 * PROCESO 1: Registrar y consultar una publicación de venta o intercambio.
 * Cabecera: Publication. Detalle: PublicationExchange.
 * Por cada operación REST: un test exitoso y un test fallido (más los flujos alternativos A2, A3).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PublicacionProcesoTest {

    @Autowired TestRestTemplate rest;
    @Autowired RepoCategory repoCategory;
    @Autowired RepoProduct repoProduct;

    Integer productId;

    // Datos previos (precondición 2: el producto ya existe en el catálogo)
    @BeforeEach
    void crearProducto() {
        Category c = new Category();
        c.setName("Figuras " + UUID.randomUUID().toString().substring(0, 8));
        c.setCreatedBy("test");
        c = repoCategory.save(c);
        Product p = new Product();
        p.setCategory(c);
        p.setName("Figura Goku");
        p.setStock(5);
        p.setPrice(new BigDecimal("99.90"));
        p.setCreatedBy("test");
        productId = repoProduct.save(p).getId();
    }

    // ---------- helpers ----------
    private String usuarioUnico() {
        return "user-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private Map<String, Object> bodyVenta(String username, Integer idProducto) {
        Map<String, Object> b = new HashMap<>();
        b.put("username", username);
        b.put("productId", idProducto);
        b.put("title", "Vendo figura");
        b.put("description", "En buen estado");
        b.put("type", "VENTA");
        b.put("state", "ACTIVA");
        b.put("createdBy", username);
        return b;
    }

    private Map<String, Object> bodyIntercambio(String username, List<Map<String, Object>> detalle) {
        Map<String, Object> b = bodyVenta(username, productId);
        b.put("type", "INTERCAMBIO");
        b.put("exchanges", detalle);
        return b;
    }

    private Map<String, Object> detalle(String nombre, Integer cantidad) {
        Map<String, Object> d = new HashMap<>();
        d.put("requestedName", nombre);
        d.put("description", "Cualquier edición");
        d.put("quantity", cantidad);
        return d;
    }

    private ResponsePublication crearVenta(String username) {
        ResponseEntity<ResponsePublication> r = rest.postForEntity("/api/publications",
                bodyVenta(username, productId), ResponsePublication.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return r.getBody();
    }

    // ---------- POST /api/publications ----------
    @Test
    void post_ventaConProductoExistente_devuelve201() {
        String user = usuarioUnico();
        ResponseEntity<ResponsePublication> r = rest.postForEntity("/api/publications",
                bodyVenta(user, productId), ResponsePublication.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r.getBody().id()).isNotNull();
        assertThat(r.getBody().username()).isEqualTo(user);
        assertThat(r.getBody().idProduct()).isEqualTo(productId);
        assertThat(r.getBody().type()).isEqualTo("VENTA");
        assertThat(r.getBody().categoryName()).startsWith("Figuras");
        assertThat(r.getBody().exchanges()).isEmpty();
    }

    @Test
    void post_conProductoInexistente_devuelve404() {
        ResponseEntity<String> r = rest.postForEntity("/api/publications",
                bodyVenta(usuarioUnico(), 999999), String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void post_intercambioConDetalle_devuelve201ConArticulos() {
        ResponseEntity<ResponsePublication> r = rest.postForEntity("/api/publications",
                bodyIntercambio(usuarioUnico(), List.of(detalle("Figura Vegeta", 2))),
                ResponsePublication.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r.getBody().type()).isEqualTo("INTERCAMBIO");
        assertThat(r.getBody().exchanges()).hasSize(1);
        assertThat(r.getBody().exchanges().get(0).requestedName()).isEqualTo("Figura Vegeta");
        assertThat(r.getBody().exchanges().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void post_intercambioSinDetalle_devuelve400() {
        ResponseEntity<String> r = rest.postForEntity("/api/publications",
                bodyIntercambio(usuarioUnico(), List.of()), String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void post_detalleConCantidadCero_devuelve400() {
        ResponseEntity<String> r = rest.postForEntity("/api/publications",
                bodyIntercambio(usuarioUnico(), List.of(detalle("Figura Vegeta", 0))), String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void post_tipoInvalido_devuelve400() {
        Map<String, Object> b = bodyVenta(usuarioUnico(), productId);
        b.put("type", "REGALO");

        ResponseEntity<String> r = rest.postForEntity("/api/publications", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void post_sinUsername_devuelve400() {
        Map<String, Object> b = bodyVenta(usuarioUnico(), productId);
        b.remove("username");

        ResponseEntity<String> r = rest.postForEntity("/api/publications", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ---------- GET /api/publications/{id} ----------
    @Test
    void getPorId_existente_devuelve200ConDatosRelacionados() {
        ResponsePublication creada = crearVenta(usuarioUnico());

        ResponseEntity<ResponsePublication> r = rest.getForEntity(
                "/api/publications/" + creada.id(), ResponsePublication.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody().id()).isEqualTo(creada.id());
        assertThat(r.getBody().productName()).isEqualTo("Figura Goku");
        assertThat(r.getBody().categoryName()).startsWith("Figuras");
    }

    @Test
    void getPorId_inexistente_devuelve404() {
        ResponseEntity<String> r = rest.getForEntity("/api/publications/999999", String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---------- GET /api/publications/search?username= (Query 1) ----------
    @Test
    void search_conUsernameValido_devuelve200YListaDelUsuario() {
        String user = usuarioUnico();
        crearVenta(user);
        rest.postForEntity("/api/publications",
                bodyIntercambio(user, List.of(detalle("Figura Vegeta", 1))), ResponsePublication.class);
        crearVenta(usuarioUnico()); // de otro usuario: no debe aparecer

        ResponseEntity<List<ResponsePublication>> r = rest.exchange(
                "/api/publications/search?username=" + user, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponsePublication>>() {});

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).hasSize(2);
        assertThat(r.getBody()).allMatch(p -> p.username().equals(user));
        assertThat(r.getBody()).anyMatch(p -> !p.exchanges().isEmpty());
    }

    @Test
    void search_sinUsername_devuelve400() {
        ResponseEntity<String> r = rest.getForEntity("/api/publications/search", String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void search_usuarioSinPublicaciones_devuelve200ListaVacia() {
        ResponseEntity<List<ResponsePublication>> r = rest.exchange(
                "/api/publications/search?username=" + usuarioUnico(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponsePublication>>() {});

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).isEmpty();
    }

    // ---------- GET /api/publications/by-type?type= (Query 2) ----------
    @Test
    void byType_tipoValido_devuelve200YSoloEseTipo() {
        crearVenta(usuarioUnico());
        rest.postForEntity("/api/publications",
                bodyIntercambio(usuarioUnico(), List.of(detalle("Figura Vegeta", 1))), ResponsePublication.class);

        ResponseEntity<List<ResponsePublication>> r = rest.exchange(
                "/api/publications/by-type?type=INTERCAMBIO", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponsePublication>>() {});

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).isNotEmpty();
        assertThat(r.getBody()).allMatch(p -> p.type().equals("INTERCAMBIO"));
    }

    @Test
    void byType_tipoInvalido_devuelve400() {
        ResponseEntity<String> r = rest.getForEntity("/api/publications/by-type?type=REGALO", String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
