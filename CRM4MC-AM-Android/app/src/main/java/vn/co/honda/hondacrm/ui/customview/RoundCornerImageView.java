
package vn.co.honda.hondacrm.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import vn.co.honda.hondacrm.R;

/**
 * Created by CuongNV31.
 */

public class RoundCornerImageView extends AppCompatImageView {

    private float radius = 10.0f;
    private Path path;
    private RectF rect;

    public RoundCornerImageView(Context context) {
        super(context);
        init(null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
            radius = a.getFloat(R.styleable.RoundCornerImageView_radiusCorner, 10.0f);
            a.recycle();
        }
        path = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, getWidth(), getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}

