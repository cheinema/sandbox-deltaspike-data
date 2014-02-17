package net.chlab.sandbox.deltaspike.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceProducerTest {

    @Mock
    private EntityManagerFactory emfMock;

    @Mock
    private EntityManager emMock;

    @InjectMocks
    private DataSourceProducer underTest;

    @Test
    public void shouldBeCreated() {
        doReturn(emMock).when(emfMock).createEntityManager();
        assertEquals(emMock, underTest.create());
    }

    @Test
    public void shouldBeClosedIfOpen() {
        doReturn(true).when(emMock).isOpen();
        underTest.close(emMock);
        verify(emMock).close();
    }

    @Test
    public void shouldNotBeClosedIfNotOpen() {
        doReturn(false).when(emMock).isOpen();
        underTest.close(emMock);
        verify(emMock, Mockito.never()).close();
    }
}
