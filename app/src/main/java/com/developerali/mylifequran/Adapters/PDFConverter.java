package com.developerali.mylifequran.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PDFConverter {

    public static ArrayList<Bitmap> convertPdfToImages(Context context, String pdfPath) {
        ArrayList<Bitmap> images = new ArrayList<>();

        try {
            File file = new File(pdfPath);
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

            if (fileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
                int pageCount = pdfRenderer.getPageCount();

                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = pdfRenderer.openPage(i);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    images.add(bitmap);
                    page.close();
                }

                pdfRenderer.close();
                fileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
    }
}
