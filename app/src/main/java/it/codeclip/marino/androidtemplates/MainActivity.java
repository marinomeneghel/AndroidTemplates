package it.codeclip.marino.androidtemplates;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.ivTemplatePreview) ImageView ivTemplatePreview;
    @Bind(R.id.llTemplatesPreviewContainer) LinearLayout llTemplatesContainer;
    @Bind(R.id.hsvTemplates) HorizontalScrollView hsvTemplates;

    private int mScreenWidth;
    private int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_templates);
        ButterKnife.bind(this);

        getDeviceScreenSize();
        initTemplatePreviewView();
        showAllTemplatePreviews();
    }

    /**
     * Set the dimensions of the main template ImageView proportionally
     */
    private void initTemplatePreviewView() {
        int width = mScreenWidth * 50 / 100;
        int height = width * 3 / 2;
        ivTemplatePreview.getLayoutParams().width = width;
        ivTemplatePreview.getLayoutParams().height = height;
        initTemplate();
    }

    /**
     * Init the template ImageView with the base image
     */
    private void initTemplate() {
        Drawable baseImage = getResources().getDrawable(R.drawable.original);
        ivTemplatePreview.setImageDrawable(baseImage);
    }

    /**
     * Add the defined drawable resources and labels to the scroll container
     */
    private void showAllTemplatePreviews() {
        final int templateButtons[] = {
                R.drawable.original, R.drawable.sample, R.drawable.sample_1, R.drawable.sample_2,
                R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6,
                R.drawable.sample_7, R.drawable.sample_8, R.drawable.sample_9
        };
        final String templateNames[] = {
                "Original", "Sample", "Sample1", "Sample2", "Sample3", "Sample4",
                "Sample5", "Sample6", "Sample7", "Sample8", "Sample9"
        };

        // Iterate to show buttons and set to them the entity to show in the main ImageView as tag.
        for (int i=0; i<templateButtons.length; i++) {
            LinearLayout templatePreview = createTemplPreviewView(templateButtons[i], templateNames[i]);
            llTemplatesContainer.addView(templatePreview);
        }
    }

    /**
     * Creates the template preview button to put in the scrollview
     * @param template the resource template/image
     * @param templateName the label to put below the template
     * @return a ViewGroup containing the given params
     */
    private LinearLayout createTemplPreviewView(int template, String templateName) {
        Button button = new Button(this);
        int width = convertToPx(80);
        int height = width * 3 / 2;
        int margin = convertToPx(5);

        LinearLayout llContainer = new LinearLayout(this);
        llContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        llContainer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(margin, 0, margin, 0);
        button.setLayoutParams(params);
        button.setBackgroundResource(template);
        button.setOnClickListener(onTemplateClick);

        TextView tvName = new TextView(this);
        Typeface merriweather = Typeface.createFromAsset(this.getAssets(), "fonts/Merriweather.ttf");
        tvName.setTypeface(merriweather, Typeface.ITALIC);
        tvName.setTextSize(10);
        tvName.setPadding(0, 10, 0, 0);
        tvName.setGravity(Gravity.CENTER_HORIZONTAL);
        tvName.setText(templateName);

        llContainer.addView(button);
        llContainer.addView(tvName);
        return llContainer;
    }

    /**
     * Change the template in the templatePreview view
     */
    private View.OnClickListener onTemplateClick = new View.OnClickListener() {
        @Override
        public void onClick(View clickedTemplate) {
            Drawable clickedImage = clickedTemplate.getBackground();
            ivTemplatePreview.setImageDrawable(clickedImage);
            ivTemplatePreview.setBackgroundResource(R.drawable.shape_template_border);
            int templateWidth = clickedTemplate.getWidth();
            int clickedViewCoordinates[] = new int[2];
            clickedTemplate.getLocationOnScreen(clickedViewCoordinates);
            scrollScrollView( clickedViewCoordinates, templateWidth );
        }
    };

    private void scrollScrollView(int viewCoordinates[], int templateWidth) {
        int x = viewCoordinates[0];
        hsvTemplates.setSmoothScrollingEnabled(true);
        int scrollTo = x<mScreenWidth/2 ? hsvTemplates.getScrollX() - templateWidth
                : hsvTemplates.getScrollX() + templateWidth;
        hsvTemplates.smoothScrollTo( scrollTo, 0 );
    }


    private void getDeviceScreenSize() {
        // Get device's screen size
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = Math.round(size.x);
        mScreenHeight = Math.round(size.y);
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}




