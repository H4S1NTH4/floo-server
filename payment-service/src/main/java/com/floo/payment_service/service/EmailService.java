package com.floo.payment_service.service;

import com.floo.payment_service.feign.EmailInterface;
import com.floo.payment_service.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    EmailInterface emailInterface;

    public void sendSuccessEmail(String paymentId, String amount, String customerName) {
//        String html = """
//        <html>
//            <body style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
//                <div style="max-width: 600px; margin: auto; background-color: white; padding: 20px; border-radius: 10px;">
//                    <h2 style="color: #28a745;">Payment Successful ✅</h2>
//                    <p>Hi %s,</p>
//                    <p>Thank you for your payment. Here are the details:</p>
//                    <table style="width: 100%%; border-collapse: collapse;">
//                        <tr>
//                            <td><strong>Payment ID:</strong></td>
//                            <td>%s</td>
//                        </tr>
//                        <tr>
//                            <td><strong>Amount:</strong></td>
//                            <td>%s</td>
//                        </tr>
//                        <tr>
//                            <td><strong>Status:</strong></td>
//                            <td>Success</td>
//                        </tr>
//                    </table>
//                    <p style="margin-top: 20px;">If you have any questions, feel free to contact us.</p>
//                    <p>Best regards,<br/>Your Company</p>
//                </div>
//            </body>
//        </html>
//    """.formatted(customerName, paymentId, amount);

        Email email = new Email();
        email.setTo("mpsjayasekara@gmail.com");
        email.setSubject("Payment Successful");
        email.setText("Payment Successful for paymentId: " + paymentId + " with amount: " + amount + " for customer: " + customerName + " ");
        emailInterface.sendEmail(email);
    }


}