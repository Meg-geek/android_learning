package com.nsu.db.aircraft.view;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.nsu.db.aircraft.R;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TableFragment extends Fragment {
    protected final static String NULL_VALUE = "null";
    private List<String> columnNames;

    public TableFragment(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    protected void showError() {
        Toast.makeText(getContext(), R.string.error_text, Toast.LENGTH_LONG).show();
    }

    protected void addColumnNames(TableLayout tableLayout) {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        for (String columnName : columnNames) {
            tableRow.addView(getTextViewForTable(columnName));
        }
        tableLayout.addView(tableRow);
    }

    protected TextView getTextViewForTable(String text) {
        TextView textView = new TextView(getContext());
        textView.setTextAppearance(R.style.TablesText);
        textView.setText(text);
        return textView;
    }
}
