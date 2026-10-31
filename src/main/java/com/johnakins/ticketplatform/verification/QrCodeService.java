package com.johnakins.ticketplatform.verification;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
@Service
public class QrCodeService {
 public String asBase64Png(String value){
  try { BitMatrix matrix=new MultiFormatWriter().encode(value,BarcodeFormat.QR_CODE,300,300);
   ByteArrayOutputStream out=new ByteArrayOutputStream(); MatrixToImageWriter.writeToStream(matrix,"PNG",out);
   return Base64.getEncoder().encodeToString(out.toByteArray());
  } catch(Exception e){throw new IllegalStateException("Could not generate QR code",e);}
 }
}
