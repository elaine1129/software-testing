package st.assignment;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderApplicationLoginAddressTest {

    private User[] mockUserArr = {
                                  new Member("1", "test", "test", "1", new Address()),
                                  new Member("2", "aName", "aPassword", "1", new Address())
                                 };
    private Address aMelakaAddress = new Address("1", "Jalan 2", "70549", "Bukit Beruang", "A district", "Melaka");
    private Address notAMelakaAddress = new Address("1", "Jalan 2", "70549", "Not Melaka Area", "A district", "Not Melaka");

    private DeliveryRates[] mockDeliveryRateData = {new DeliveryRates("Bukit Beruang", 2)};

    //login test ---
    @Test
    public void testValidUserLogin(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        //select 1 choice (login)
        when(mockScanner.nextInt()).thenReturn(1);
        when(mockScanner.next()).thenReturn("test").thenReturn("test");

        when(mockAppData.getUserArr()).thenReturn(mockUserArr);

        OrderApplication order = new OrderApplication(mockScanner, mockAppData);
        assertTrue(order.login());

        verify(mockAppData).setLoggedInUser(mockUserArr[0]);
    }

    @Test
    public void testWrongUsernameWrongPasswordUserLogin(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        //select 1 choice (login)
        when(mockScanner.nextInt()).thenReturn(1);
        //enter wrong name and password
        when(mockScanner.next()).thenReturn("WrongName").thenReturn("WrongPassword");

        when(mockAppData.getUserArr()).thenReturn(mockUserArr);

        OrderApplication order = new OrderApplication(mockScanner, mockAppData);
        assertFalse(order.login());

        //only used to logged out user
        verify(mockAppData, atMost(1)).setLoggedInUser(anyObject());
    }

    @Test
    public void testGuestLogin(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        //select 2 choice (guest)
        when(mockScanner.nextInt()).thenReturn(2);
        //enter a guest name
        when(mockScanner.nextLine()).thenReturn("GuestName")
                //enter melaka address
                                    .thenReturn(aMelakaAddress.getUnitNumber())
                                    .thenReturn(aMelakaAddress.getStreetName())
                                    .thenReturn(aMelakaAddress.getPostalCode())
                                    .thenReturn(aMelakaAddress.getArea())
                                    .thenReturn(aMelakaAddress.getDistrict());

        when(mockAppData.getDeliveryRatesArr()).thenReturn(mockDeliveryRateData);

        OrderApplication order = new OrderApplication(mockScanner, mockAppData);
        assertTrue(order.login());

        //only used to logged out user
        verify(mockAppData, atMost(1)).setLoggedInUser(new Guest("GuestName", aMelakaAddress));
    }

    @Test
    public void testSignUpLogin(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        Member newMember = new Member ("ID", "new name", "new password", "a phone number", aMelakaAddress);

        //select 3 choice (sign up) then select exit
        when(mockScanner.nextInt()).thenReturn(3).thenReturn(4);
        //enter a guest name
        when(mockScanner.nextLine()).thenReturn(newMember.getName())
                .thenReturn(newMember.getPassword())
                .thenReturn(newMember.getPhoneNumber())
                .thenReturn(aMelakaAddress.getUnitNumber())
                .thenReturn(aMelakaAddress.getStreetName())
                .thenReturn(aMelakaAddress.getPostalCode())
                .thenReturn(aMelakaAddress.getArea())
                .thenReturn(aMelakaAddress.getDistrict());
        when(mockAppData.getDeliveryRatesArr()).thenReturn(mockDeliveryRateData);

        when(mockAppData.newMemberID()).thenReturn(newMember.getMemberID());

        OrderApplication order = new OrderApplication(mockScanner, mockAppData);
        order.login();

        //verify is the new member added
        verify(mockAppData, atMost(1)).addNewMember(newMember);
    }
    //login test --- END

    //Address test ---
    @Test
    public void testAddress(){
        MyScanner mockScanner = mock(MyScanner.class);
        ApplicationData mockAppData = mock(ApplicationData.class);

        when(mockScanner.nextLine()).thenReturn(aMelakaAddress.getUnitNumber())
                .thenReturn(aMelakaAddress.getStreetName())
                .thenReturn(aMelakaAddress.getPostalCode())

                //test 1 time not valid
                .thenReturn(notAMelakaAddress.getArea())

                //enter valid address
                .thenReturn(aMelakaAddress.getArea())
                .thenReturn(aMelakaAddress.getDistrict());
        when(mockAppData.getDeliveryRatesArr()).thenReturn(mockDeliveryRateData);

        OrderApplication order = new OrderApplication(mockScanner, mockAppData);
        assertEquals(aMelakaAddress, order.get_address_info());

        //verify is the check delivery rates is called 2 times
        verify(mockAppData, times(2)).getDeliveryRatesArr();
    }
    //Address test END ---
}