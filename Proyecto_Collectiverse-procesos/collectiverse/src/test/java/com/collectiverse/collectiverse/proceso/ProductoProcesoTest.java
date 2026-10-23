package com.collectiverse.collectiverse.proceso;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.collectiverse.collectiverse.cu.product.response.ResponseProduct;
import com.collectiverse.collectiverse.dominio.entity.Category;
import com.collectiverse.collectiverse.dominio.entity.CollectionEntity;
import com.collectiverse.collectiverse.dominio.repository.RepoCategory;
import com.collectiverse.collectiverse.dominio.repository.RepoCollection;

/**
 * PROCESO 2: Registrar y consultar un producto del catálogo.
 * Cabecera: Product. Detalle: ProductImage.
 * Por cada operación REST: un test exitoso y un test fallido (más los flujos alternativos B3, B4).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoProcesoTest {

    @Autowired TestRestTemplate rest;
    @Autowired RepoCategory repoCategory;
    @Autowired RepoCollection repoCollection;

    Integer categoryId;
    Integer collectionId;
    String sufijo; // hace únicos los nombres para que los tests no se mezclen entre sí

    // Datos previos (precondiciones 2 y 3: categoría y colección existentes)
    @BeforeEach
    void crearCategoriaYColeccion() {
        sufijo = UUID.randomUUID().toString().substring(0, 8);
        Category c = new Category();
        c.setName("Manga " + sufijo);
        c.setCreatedBy("test");
        c = repoCategory.save(c);
        categoryId = c.getId();

        CollectionEntity col = new CollectionEntity();
        col.setCategory(c);
        col.setName("Dragon Ball " + sufijo);
        col.setCreatedBy("test");
        collectionId = repoCollection.save(col).getId();
    }

    // ---------- helpers ----------
    private Map<String, Object> bodyProducto(Integer idCategoria, String nombre) {
        Map<String, Object> b = new HashMap<>();
        b.put("categoryId", idCategoria);
        b.put("name", nombre);
        b.put("stock", 10);
        b.put("price", 49.90);
        b.put("createdBy", "admin");
        return b;
    }

    private Map<String, Object> imagen(String url, Integer orden) {
        Map<String, Object> i = new HashMap<>();
        i.put("url", url);
        i.put("order", orden);
        return i;
    }

    private ResponseProduct crearProducto(String nombre) {
        ResponseEntity<ResponseProduct> r = rest.postForEntity("/api/products",
                bodyProducto(categoryId, nombre), ResponseProduct.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return r.getBody();
    }

    private ResponseEntity<List<ResponseProduct>> buscar(String queryString) {
        return rest.exchange("/api/products/search" + queryString, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponseProduct>>() {});
    }

    // ---------- POST /api/products ----------
    @Test
    void post_conCategoriaExistenteEImagenValida_devuelve201() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 1 " + sufijo);
        b.put("collectionId", collectionId);
        b.put("images", List.of(imagen("https://img.test/a.jpg", 1), imagen("https://img.test/b.jpg", 2)));

        ResponseEntity<ResponseProduct> r = rest.postForEntity("/api/products", b, ResponseProduct.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r.getBody().id()).isNotNull();
        assertThat(r.getBody().categoryId()).isEqualTo(categoryId);
        assertThat(r.getBody().categoryName()).isEqualTo("Manga " + sufijo);
        assertThat(r.getBody().collectionId()).isEqualTo(collectionId);
        assertThat(r.getBody().collectionName()).isEqualTo("Dragon Ball " + sufijo);
        assertThat(r.getBody().images()).hasSize(2);
        assertThat(r.getBody().images().get(0).url()).isEqualTo("https://img.test/a.jpg");
    }

    @Test
    void post_conCategoriaInexistente_devuelve404() {
        ResponseEntity<String> r = rest.postForEntity("/api/products",
                bodyProducto(999999, "Tomo 1"), String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void post_conColeccionInexistente_devuelve404() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 1");
        b.put("collectionId", 999999);

        ResponseEntity<String> r = rest.postForEntity("/api/products", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void post_conStockNegativo_devuelve400() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 1");
        b.put("stock", -1);

        ResponseEntity<String> r = rest.postForEntity("/api/products", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void post_sinCreatedBy_devuelve400() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 1");
        b.remove("createdBy");

        ResponseEntity<String> r = rest.postForEntity("/api/products", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void post_conImagenSinUrl_devuelve400() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 1");
        b.put("images", List.of(imagen("", 1)));

        ResponseEntity<String> r = rest.postForEntity("/api/products", b, String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ---------- GET /api/products/{id} ----------
    @Test
    void getPorId_existente_devuelve200ConRelaciones() {
        Map<String, Object> b = bodyProducto(categoryId, "Tomo 2 " + sufijo);
        b.put("collectionId", collectionId);
        b.put("images", List.of(imagen("https://img.test/c.jpg", 1)));
        ResponseProduct creado = rest.postForEntity("/api/products", b, ResponseProduct.class).getBody();

        ResponseEntity<ResponseProduct> r = rest.getForEntity("/api/products/" + creado.id(), ResponseProduct.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody().id()).isEqualTo(creado.id());
        assertThat(r.getBody().categoryName()).isEqualTo("Manga " + sufijo);
        assertThat(r.getBody().collectionName()).isEqualTo("Dragon Ball " + sufijo);
        assertThat(r.getBody().images()).hasSize(1);
    }

    @Test
    void getPorId_inexistente_devuelve404() {
        ResponseEntity<String> r = rest.getForEntity("/api/products/999999", String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ---------- GET /api/products/search?categoryId= (Query 1) ----------
    @Test
    void search_porCategoriaExistente_devuelve200YListaDeEsaCategoria() {
        crearProducto("Tomo A " + sufijo);
        crearProducto("Tomo B " + sufijo);

        ResponseEntity<List<ResponseProduct>> r = buscar("?categoryId=" + categoryId);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).hasSize(2);
        assertThat(r.getBody()).allMatch(p -> p.categoryId().equals(categoryId));
    }

    @Test
    void search_sinCategoryIdNiTerm_devuelve400() {
        ResponseEntity<String> r = rest.getForEntity("/api/products/search", String.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void search_categoriaSinProductos_devuelve200ListaVacia() {
        Category vacia = new Category();
        vacia.setName("Vacia " + sufijo);
        vacia.setCreatedBy("test");
        Integer idVacia = repoCategory.save(vacia).getId();

        ResponseEntity<List<ResponseProduct>> r = buscar("?categoryId=" + idVacia);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).isEmpty();
    }

    // ---------- GET /api/products/search?term= (Query 2) ----------
    @Test
    void search_porTerminoValido_devuelve200SinDistinguirMayusculas() {
        crearProducto("Manga Naruto " + sufijo);
        crearProducto("Figura Zoro " + sufijo);

        ResponseEntity<List<ResponseProduct>> r = buscar("?term=NARUTO " + sufijo);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).hasSize(1);
        assertThat(r.getBody().get(0).name()).isEqualTo("Manga Naruto " + sufijo);
    }

    @Test
    void search_terminoSinCoincidencias_devuelve200ListaVacia() {
        ResponseEntity<List<ResponseProduct>> r = buscar("?term=zzz-" + sufijo);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).isEmpty();
    }
}
