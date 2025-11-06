package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import ru.netology.web.data.DataHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.LoginPage;

import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    void shouldTransferFromFirstToSecondCard() {
        var LoginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(getFirstCardNumber().getCardNumber());
        var secondCardBalance = dashboardPage.getCardBalance(getSecondCardNumber().getCardNumber());
        var transferPage = dashboardPage.depositToFirstCard();
        var amount =generateValidAmount(firstCardBalance);
        transferPage.transferMoney(amount, getSecondCardNumber());
        var expectedFirstCardBalanceAfter = firstCardBalance + amount;
        var expectedSecondCardBalanceAfter = secondCardBalance - amount;
        Assertions.assertEquals(expectedFirstCardBalanceAfter, dashboardPage.getCardBalance(getFirstCardNumber().getCardNumber()));
        Assertions.assertEquals(expectedSecondCardBalanceAfter, dashboardPage.getCardBalance(getSecondCardNumber().getCardNumber()));
    }

}