package com.newthread.campusbroadcast.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.newthread.campusbroadcast.R;

/**
 * Created by 倪启航 on 2017/7/28.
 */

public class ReportDialog {

    Context act = null;
    private int result = 7;
    private ReportResult reportResult;

    public ReportDialog(Context act) {
        this.act = act;
    }

    public void show(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("选择一项举报该电台:");
        builder.setIcon(R.drawable.report_icon);
        final String[] items = new String[]{"含有暴力色情等内容。", "言论有不当、虚假内容。", "含有引起不适的内容。"};
        builder.setSingleChoiceItems(items,3, new DialogInterface.OnClickListener() {/*设置单选条件的点击事件*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(act, "感谢您的举报~", Toast.LENGTH_SHORT).show();
                reportResult.getResult(result);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(act, "举报取消啦~", Toast.LENGTH_SHORT).show();
                result = 7;
                reportResult.getResult(result);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public interface ReportResult{

        public void getResult(int result);
    }

    public void getReportResultListener(ReportResult reportResult){

        this.reportResult=reportResult;
    }
}
