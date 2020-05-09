package protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;
import protect.FinanceLord.R;
import protect.FinanceLord.NetWorthEditReportsUtils.NetWorthExpandableListView;

public class AssetsFragmentAdapter extends BaseExpandableListAdapter {

    private DataProcessor_Assets dataProcessor;
    private TypeProcessor_Assets typeProcessor;
    private List<DataCarrier_Assets> sectionDataSet;
    private int level;
    private Context context;

    public AssetsFragmentAdapter(Context context, DataProcessor_Assets dataProcessor, TypeProcessor_Assets typeProcessor, int level, String parentSection) {
        this.context = context;
        this.dataProcessor = dataProcessor;
        this.typeProcessor = typeProcessor;
        this.level = level;
        this.sectionDataSet = typeProcessor.getSubGroup(parentSection, level);
    }

    public String getAssetsName(int position) {
        return this.sectionDataSet.get(position).assetsTypeName;
    }

    @Override
    public int getGroupCount() {
        return sectionDataSet.size();
    }

    @Override
    public int getChildrenCount(int i1) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return sectionDataSet.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String assetsTypeName = getAssetsName(i);
        List<DataCarrier_Assets> carriers = typeProcessor.getSubGroup(assetsTypeName, level + 1);
        return carriers.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (level == 1){
            convertView = inflater.inflate(R.layout.assets_list_row_second_items, null);
            TextView textView = convertView.findViewById(R.id.assetsRowSecondText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 2 && sectionDataSet.get(position).assetsId != 29
                              && sectionDataSet.get(position).assetsId != 30
                              && sectionDataSet.get(position).assetsId != 31){
            convertView = inflater.inflate(R.layout.assets_list_row_third_items, null);
            TextView textView = convertView.findViewById(R.id.assetsRowThirdText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);

            DataCarrier_Assets dataCarrier = this.sectionDataSet.get(position);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);

            AssetsValue assetsValue = dataProcessor.getAssetsValue(dataCarrier.assetsId);
            if (assetsValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(assetsValue.getAssetsValue());
                editText.setText(strValue);
            }

            this.addTextListener(editText, dataCarrier);
        } else if (level == 2) {
            convertView = inflater.inflate(R.layout.assets_list_row_second_category, null);
            TextView textView = convertView.findViewById(R.id.assetsRowSecondCategoryText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 3) {
            convertView = inflater.inflate(R.layout.assets_list_row_third_items, null);
            TextView textView = convertView.findViewById(R.id.assetsRowThirdText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);

            DataCarrier_Assets dataCarrier = this.sectionDataSet.get(position);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);

            AssetsValue assetsValue = dataProcessor.getAssetsValue(dataCarrier.assetsId);
            if (assetsValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(assetsValue.getAssetsValue());
                editText.setText(strValue);
            }

            this.addTextListener(editText, dataCarrier);
        }
        return convertView;
    }

    void addTextListener(EditText editText, final DataCarrier_Assets dataCarrier) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)){
                    String numberStr = text.replace(",", "");
                    final float assetValue = Float.parseFloat(numberStr);

                    dataProcessor.setAssetValue(dataCarrier.assetsId, assetValue);
                    Log.d("AFragmentAdapter", "Value changed: " + text + ", float value: " + assetValue);
                } else {
                    String numberStr = "0.00";
                    final float assetsValue = Float.parseFloat(numberStr);

                    dataProcessor.setAssetValue(dataCarrier.assetsId, assetsValue);
                    Log.d("AFragmentAdapter","Value empty, set to 0 " + ", float value: " + assetsValue);
                }
            }
        });
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final DataCarrier_Assets sectionData = sectionDataSet.get(groupPosition);
        List<DataCarrier_Assets> children = typeProcessor.getSubGroup(sectionData.assetsTypeName, level + 1);

        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assets_list_row_first, null);
            TextView textView = convertView.findViewById(R.id.assetsRowParentText);
            textView.setText(this.sectionDataSet.get(childPosition).assetsTypeName);
            return convertView;
        } else {
            final NetWorthExpandableListView nextLevelExpandableListView = new NetWorthExpandableListView(context);
            AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(sectionDataSet, dataProcessor, typeProcessor, level + 1);
            nextLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, dataProcessor, typeProcessor,level + 1, sectionData.assetsTypeName));
            nextLevelExpandableListView.setOnChildClickListener(listener);
            nextLevelExpandableListView.setGroupIndicator(null);
            nextLevelExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousGroup){
                        nextLevelExpandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                }
            });

            return nextLevelExpandableListView;
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}