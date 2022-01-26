import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.runners.Parameterized;
import testhelper.DummySimpleLogger;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BubbleTeaOrderServiceTest {
    private DebitCard testDebitCard;
    private PaymentDetails paymentDetails;
    private DummySimpleLogger dummySimpleLogger;
    private BubbleTeaMessenger mockMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;
    private String name;
    private String address;
    private String number;
    private BubbleTeaTypeEnum bubbleTeaTypeValue;

    @Before
    public void setup() {
        testDebitCard = new DebitCard(number);
        paymentDetails = new PaymentDetails(name, address, testDebitCard);
        dummySimpleLogger = new DummySimpleLogger();
        mockMessenger = mock(BubbleTeaMessenger.class);
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger,
                mockMessenger);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = {{"hello kitty", "sanrio puroland", "0123456789", BubbleTeaTypeEnum.OolongMilkTea},
                {"Emily", "paris", "123456", BubbleTeaTypeEnum.JasmineMilkTea},
                {"John", "london", "3456321", BubbleTeaTypeEnum.JasmineMilkTea}};
        return Arrays.asList(data);
    }

    public BubbleTeaOrderServiceTest(String name, String address, String
            number, BubbleTeaTypeEnum bubbleTeaTypeValue) {
        this.name = name;
        this.address = address;
        this.number = number;
        this.bubbleTeaTypeValue = bubbleTeaTypeValue;
    }

    @Test
    public void shouldCreateBubbleTeaOrderRequestWhenCreateOrderRequestIsCalled() {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(bubbleTeaTypeValue, 4.5);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails,
                bubbleTea);
        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                name,
                address,
                number,
                bubbleTeaTypeValue
        );
        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);
        //Assert
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.getAddress(), result.getAddress());
        assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());
        //Verify Mock was called with the BubbleTeaOrderRequest result object
        verify(mockMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(mockMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }
}