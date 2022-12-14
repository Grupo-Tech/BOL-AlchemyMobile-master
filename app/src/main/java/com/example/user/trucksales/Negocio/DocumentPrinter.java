package com.example.user.trucksales.Negocio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.loopj.android.http.RequestParams;

/**
 * Created by Joaco on 7/10/2018.
 */

public class DocumentPrinter extends PrintDocumentAdapter {
    private Context context;
    private PrintedPdfDocument mPdfDocument;
    private RequestParams params;

    public DocumentPrinter(Context context) {
        this.context = context;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

        // create new PDF with the requested page attributes
        mPdfDocument = new PrintedPdfDocument( context, newAttributes );
        // respond to cancellation request
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        int pages = computePageCount( newAttributes );

        if (pages > 0) {
            // return print information to print framework
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder( "print_output.pdf" )
                    .setContentType( PrintDocumentInfo.CONTENT_TYPE_DOCUMENT )
                    .setPageCount( pages )
                    .build();
            // second argument indicates if layout has changed - if not cached one is used
            callback.onLayoutFinished( info, true );
        } else {
            callback.onLayoutFailed( "Page count calculation failed." );
        }

    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4; //default item count for portrait mode
        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if (!pageSize.isPortrait()) {
            itemsPerPage = 6; // 6 items per page in landscape mode
        }

        int printItemCount = getPrintItemCount();
        return (int) Math.ceil( printItemCount / itemsPerPage );
    }

    private int getPrintItemCount() {

        return 0;
    }

    private boolean containsPage(PageRange[] pages, int i) {
        // check if contains
        return true;
    }
    /*@Override
    public void onStart()
    {

    }*/
    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        int totalPages = 10;
        for (int i = 0; i < totalPages; i++) {
            // Check to see if this page is in the output range.
            if (containsPage( pages, i )) {
                PdfDocument.Page[] writtenPagesArray;
                // If so, add it to writtenPagesArray. writtenPagesArray.size()
                // is used to compute the next output page index.
                //writtenPagesArray.append(writtenPagesArray.size(), i);
                PdfDocument.Page page = mPdfDocument.startPage( i );

                // check for cancellation
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }

                // Draw page content for printing
                drawPage( page );

                // Rendering is complete, so page can be finalized.
                mPdfDocument.finishPage( page );

            }
        }
    }
    @Override
    public void onFinish()
    {
        params = new RequestParams(  );
        params.add( "ruta","3686" );
        /*BorrarArchivo task = new BorrarArchivo();
        try
        {
            task.execute(  );
        }
        catch (Exception ex)
        {
            ex.toString();
        }*/
    }
    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();

        // units are in points (1/72 of an inch)
        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText("Test Title", leftMargin, titleBaseLine, paint);

        paint.setTextSize(11);
        canvas.drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint);

        paint.setColor( Color.BLUE);
        canvas.drawRect(100, 100, 172, 172, paint);
    }

}

