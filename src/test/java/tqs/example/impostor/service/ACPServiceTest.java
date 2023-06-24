package tqs.example.impostor.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.example.impostor.models.ACP;
import tqs.example.impostor.repository.ACPRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ACPServiceTest {
    @Mock
    private ACPRepository acpRepository;

    private ACPService acpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        acpService = new ACPService(acpRepository);
    }

    @Test
    void createACP_ValidData_ReturnsTrue() {
        Long id = 1L;
        String address = "piwosz";
        double capacity = 10.0;
        boolean result = acpService.createACP(id, address, capacity);
        assertTrue(result);
    }

    @Test
    void createACP_NullAddress_ReturnsFalse() {
        Long id = 1L;
        String address = null;
        double capacity = 10.0;
        boolean result = acpService.createACP(id, address, capacity);
        assertFalse(result);
    }

    @Test
    void createACP_ZeroCapacity_ReturnsFalse() {
        Long id = 1L;
        String address = "piwosz";
        double capacity = 0.0;
        boolean result = acpService.createACP(id, address, capacity);
        assertFalse(result);
    }

    @Test
    void createACP_AlreadyExists_ReturnsFalse() {
        Long id = 1L;
        String address = "piwosz";
        double capacity = 10.0;
        when(acpRepository.existsById(id)).thenReturn(true);
        boolean result = acpService.createACP(id, address, capacity);
        assertFalse(result);
    }

    @Test
    void searchACPbyID_ExistingID_ReturnsOptionalACP() {
        Long id = 1L;
        ACP expectedACP = new ACP();
        when(acpRepository.findById(id)).thenReturn(Optional.of(expectedACP));
        Optional<ACP> result = acpService.getACPById(id);
        assertEquals(Optional.of(expectedACP), result);
    }

    @Test
    void searchACPbyID_NonExistingID_ReturnsEmptyOptional() {
        Long id = 1L;
        when(acpRepository.findById(id)).thenReturn(Optional.empty());
        Optional<ACP> result = acpService.getACPById(id);
        assertEquals(Optional.empty(), result);
    }

    @Test
    void whenGetAllACPs_thenReturnListOfACPs() {
        ACP acp = new ACP();

        List<ACP> acpList = new ArrayList<>();
        acpList.add(acp);

        when(acpRepository.findAll()).thenReturn(acpList);

        assertEquals(acpService.getAllACPs(), acpList);
    }

    @Test
    void givenAddress_whenGetACPByAddress_thenReturnACP() {
        ACP acp = new ACP();

        when(acpRepository.findByAddress(Mockito.any())).thenReturn(Optional.of(acp));

        assertEquals(acpService.getACPByAddress("add"), Optional.of(acp));
    }

    @Test
    void givenNewParameters_whenUpdateACP_thenReturnTrue() {
        Long id = 1L;
        ACP expectedACP = new ACP();
        when(acpRepository.findById(id)).thenReturn(Optional.of(expectedACP));

        //Set<Order> orderSet = new HashSet<>();

        assertTrue(acpService.updateACP(id, "add", 1f));
    }

    @Test
    void givenAcceptableNullParameters_whenUpdateACP_thenReturnTrue() {
        Long id = 1L;
        ACP expectedACP = new ACP();
        when(acpRepository.findById(id)).thenReturn(Optional.of(expectedACP));

        assertTrue(acpService.updateACP(id, null, -1f));
    }

    @Test
    void givenACPId_whenDeleteACP_thenReturnTrue() {
        long id = 1L;
        when(acpRepository.existsById(id)).thenReturn(true);

        assertTrue(acpService.deleteACP(id));
    }

    @Test
    void givenWrongId_whenDeleteACP_thenReturnFalse() {
        when(acpRepository.existsById(Mockito.any())).thenReturn(false);
        assertFalse(acpService.deleteACP(15L));
    }
}
