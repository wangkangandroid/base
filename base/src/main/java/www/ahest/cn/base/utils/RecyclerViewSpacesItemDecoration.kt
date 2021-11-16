package www.ahest.cn.base.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerViewSpacesItemDecoration(private val mSpaceValueMap: HashMap<String, Int>) :
    RecyclerView.ItemDecoration() {

    companion object {
        val TOP_DECORATION = "top_decoration"
        val BOTTOM_DECORATION = "bottom_decoration"
        val LEFT_DECORATION = "left_decoration"
        val RIGHT_DECORATION = "right_decoration"
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mSpaceValueMap.containsKey(TOP_DECORATION)) {
            outRect.top = mSpaceValueMap[TOP_DECORATION]!!
        }
        if (mSpaceValueMap.containsKey(LEFT_DECORATION)) {
            outRect.left = mSpaceValueMap[LEFT_DECORATION]!!
        }
        if (mSpaceValueMap.containsKey(RIGHT_DECORATION)) {
            outRect.right = mSpaceValueMap[RIGHT_DECORATION]!!
        }
        if (mSpaceValueMap.containsKey(BOTTOM_DECORATION)) {
            outRect.bottom = mSpaceValueMap[BOTTOM_DECORATION]!!
        }

    }


}