package com.prv.pressshare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prv.pressshare.R;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.FileDownloader;
import com.prv.pressshare.views.FileInterface;
import java.io.File;
import java.io.IOException;

/**
 * Created by roger on 31/10/2017.
 */

public class HelpActivity extends AppCompatActivity  {

    /**
     * Key string for saving the state of current page index.
     */
    private final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * {@link PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;

    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;

    /**
     * {@link ImageView} that shows a PDF page as a {@link Bitmap}
     */
    private ImageView mIBRenderImage;

    /**
     * {@link Button} to move to the previous page.
     */
    private Button mIBRenderPrevious;

    /**
     * {@link Button} to move to the next page.
     */
    private Button mIBRenderNext;

    /**
     * PDF page index
     */
    private int mPageIndex;

    private String mFilename;

    private Config mConfig = Config.sharedInstance();

    private TextView mIBHelpTitle;

    private String  mUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Retain view references.
        mIBRenderImage = (ImageView) findViewById(R.id.IBRenderImage);
        mIBRenderPrevious = (Button) findViewById(R.id.IBRenderPrevious);
        mIBRenderNext = (Button) findViewById(R.id.IBRenderNext);
        mIBHelpTitle = (TextView) findViewById(R.id.IBHelpTitle);

        mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }

        mFilename  =  getIntent().getStringExtra(mConfig.getDomaineApp()+"helpTitre");

        mUrl = mConfig.getUrlServer()+"Tuto_PressShare/"+mFilename+".pdf";


        FileDownloader fileDownloader = new FileDownloader(this, new FileInterface() {
            @Override
            public void completionHandlerFile(Boolean success, final File file) {

                if (success) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(HelpActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            try {
                                openRenderer(file);
                                showPage(mPageIndex);
                            } catch (IOException e) {
                                e.printStackTrace();
                                MyTools.sharedInstance().displayAlert(HelpActivity.this,"Error! " + e.getMessage());
                            }

                        }
                    });

                }

            }
        });

        fileDownloader.execute(mUrl);



    }


    /**
     * Sets up a {@link PdfRenderer} and related resources.
     */
    private void openRenderer(File file) throws IOException {

        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }

    }

    /**
     * Closes the {@link PdfRenderer} and related resources.
     *
     * @throws IOException When the PDF file cannot be closed.
     */
    private void closeRenderer() throws IOException {

        if (null != mCurrentPage) {
            mCurrentPage.close();
        }

        if (null != mPdfRenderer) {
            mPdfRenderer.close();
        }

        if (null != mFileDescriptor) {
            mFileDescriptor.close();
        }

    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private void showPage(int index) {

        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        mIBRenderImage.setImageBitmap(bitmap);
        updateUi();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();
        mIBRenderPrevious.setEnabled(0 != index);
        mIBRenderNext.setEnabled(index + 1 < pageCount);

        mIBHelpTitle.setText(getString(R.string.help_with_index, index + 1, pageCount));
    }

    /**
     * Gets the number of pages in the PDF. This method is marked as public for testing.
     *
     * @return The number of pages.
     */
    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }


    public void actionRenderPrevious(View view) {

        // Move to the previous page
        showPage(mCurrentPage.getIndex() - 1);

    }

    public void actionRenderNext(View view) {

        // Move to the next page
        showPage(mCurrentPage.getIndex() + 1);

    }


    public void actionCloseWidows(View view) {

        try {
            closeRenderer();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void actionHeedback(View view) {

        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);


    }




}
