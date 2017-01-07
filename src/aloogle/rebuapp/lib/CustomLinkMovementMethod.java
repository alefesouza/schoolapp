package aloogle.rebuapp.lib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.style.URLSpan;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;

public class CustomLinkMovementMethod extends LinkMovementMethod
{

private static Context movementContext;

private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event)
{
    int action = event.getAction();

    if (action == MotionEvent.ACTION_UP)
    {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
        if (link.length != 0) {
            String url = link[0].getURL();
            if (url.contains("aloogle")) {
				Intent intent = new Intent(movementContext, aloogle.rebuapp.activity.FragmentActivity.class);
				intent.putExtra("fragment", 5);
				String[] palavra = url.split("=");
				intent.putExtra("palavra", palavra[palavra.length - 1]);
				movementContext.startActivity(intent);
            } else {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				movementContext.startActivity(intent);
			}
            return true;
        }
    }

    return super.onTouchEvent(widget, buffer, event);
}

	public static android.text.method.MovementMethod getInstance(Context c)
	{
		movementContext = c;
		return linkMovementMethod;
	}
}
