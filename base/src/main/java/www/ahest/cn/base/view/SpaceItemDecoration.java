package www.ahest.cn.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int leftRight;
    private final int topBottom;

    //leftRight为横向间的距离 topBottom为纵向间距离
    public SpaceItemDecoration(Context context, int leftRight, int topBottom) {
//        this.leftRight = UnitConverseUtils.px2dp(leftRight);
//        this.topBottom = UnitConverseUtils.px2dp(topBottom);
        this.leftRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftRight, context.getResources().getDisplayMetrics());
        this.topBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topBottom, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.right = leftRight;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.bottom = topBottom;
        }
    }

}
