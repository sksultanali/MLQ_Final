package com.developerali.mylifequran.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PdfToImageConverter {

    public interface PdfConversionListener {
        void onConversionComplete(ArrayList<Bitmap> images);

        void onConversionError(Exception e);
    }

    public static void convertPdfToImages(Context context, String pdfUrl, PdfConversionListener listener) {
        new PdfConversionTask(context, pdfUrl, listener).execute();
    }

    private static class PdfConversionTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
        private final Context context;
        private final String pdfUrl;
        private final PdfConversionListener listener;

        public PdfConversionTask(Context context, String pdfUrl, PdfConversionListener listener) {
            this.context = context;
            this.pdfUrl = pdfUrl;
            this.listener = listener;
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... params) {
            ArrayList<Bitmap> images = new ArrayList<>();

            try {
                // Download PDF from URL
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();
                File file = new File(context.getCacheDir(), "temp_pdf.pdf");
                FileOutputStream output = new FileOutputStream(file);

                byte[] buffer = new byte[4 * 1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

                // Convert PDF to images
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

                // Clean up temporary file
                if (file.exists()) {
                    file.delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onConversionError(e);
                }
            }

            return images;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> images) {
            super.onPostExecute(images);
            if (listener != null) {
                listener.onConversionComplete(images);
            }
        }
    }
}