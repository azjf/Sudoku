package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class PuzzleView extends View {
    private static final String TAG = "Sudoku";
    private final Game game;

    public PuzzleView(Context context) {
        super(context);

        this.game = (Game) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    private float width;
    private float height;
    private int selX;
    private int selY;
    private final Rect selRect = new Rect();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;
        getRect(selX, selY, selRect);
        Log.d(TAG, "onSizeChanged: width " + width + ", height "
                + height);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint backgroud = new Paint();
        backgroud.setColor(getResources().getColor(
                R.color.puzzle_backgroud
        ));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroud);


        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        for (int i=0; i<9; i++) {
            canvas.drawLine(0, i * height, getWidth(), i * height,
                    light);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height
                + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(),
                    light);
            canvas.drawLine(i * width + 1, 0, i * width +1,
                    getHeight(), hilite);
        }

        for (int i=0; i<9; i++) {
            if (i % 3 != 0)
                continue;
            canvas.drawLine(0, i * height, getWidth(), i * height,
                    dark);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height
                    + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(),
                    dark);
            canvas.drawLine(i * width + 1, 0, i * width +1,
                    getHeight(), hilite);
        }


        Paint foregroud = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroud.setColor(getResources().getColor(
                R.color.puzzle_foreground
        ));
        foregroud.setStyle(Paint.Style.FILL);
        foregroud.setTextSize(height * 0.75f);
        foregroud.setTextScaleX(width / height);
        foregroud.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = foregroud.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;

        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                canvas.drawText("6", //this.game.getTileString(i, j),
                        i * width + x, j * height + y, foregroud);
            }
        }


        Log.d(TAG, "selRect=" + selRect);
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(
                R.color.puzzle_selected
        ));
        canvas.drawRect(selRect, selected);
    }


    private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * width), (int) (y * height), (int) (x
            * width + width), (int) (y * height + height));
    }

    private void select(int x, int y) {
        invalidate(selRect);
        selX = Math.min(Math.max(x, 0), 8);
        selY = Math.min(Math.max(y, 0), 8);
        getRect(selX, selY, selRect);
        invalidate(selRect);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: keycode=" + keyCode + ", event=" + event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}