package protect.FinanceLord.TransactionAdding;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Communicators.SaveDataCommunicator;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.R;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;
import protect.FinanceLord.TransactionAddActivity;
import protect.FinanceLord.TransactionUtils.TransactionDatabaseHelper;
import protect.FinanceLord.TransactionUtils.TransactionInputWidgets;
import protect.FinanceLord.ViewModels.BudgetTypesViewModel;

public class Add_TransactionsFragment extends Fragment {

    private String fragmentTag;
    private Date currentTime;
    private TransactionDatabaseHelper databaseUtils;
    private TransactionAddActivity transactionAddActivity;
    private List<BudgetsType> budgetsTypes;
    private List<String> typeNames = new ArrayList<>();
    private TransactionInputWidgets inputUtils = new TransactionInputWidgets();

    private static final String TAG = "Edit_RevenuesFragment";

    public Add_TransactionsFragment(Date currentTime, List<BudgetsType> budgetsTypes, String fragmentTag) {
        this.currentTime = currentTime;
        this.budgetsTypes = budgetsTypes;
        this.fragmentTag = fragmentTag;

        for (BudgetsType budgetsType : budgetsTypes) {
            typeNames.add(budgetsType.getBudgetsName());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        transactionAddActivity = (TransactionAddActivity) context;

        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            transactionAddActivity.toEditRevenuesCommunicator = fromActivityCommunicator;
        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            transactionAddActivity.toEditExpensesCommunicator = fromActivityCommunicator;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_revenues, null);

            setUpRevenuesInputUtils(revenuesFragmentView);

            return revenuesFragmentView;

        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            View expensesFragmentView = inflater.inflate(R.layout.fragment_edit_expenses, null);

            setUpExpensesInputUtils(expensesFragmentView);

            return expensesFragmentView;
        }

        return null;
    }

    private void setUpRevenuesInputUtils(View revenuesFragmentView) {
        inputUtils.nameInputField = revenuesFragmentView.findViewById(R.id.revenue_name_field);
        inputUtils.valueInputField = revenuesFragmentView.findViewById(R.id.revenue_value_field);
        inputUtils.categoryInputField = revenuesFragmentView.findViewById(R.id.revenue_category_field);

        inputUtils.nameInput = revenuesFragmentView.findViewById(R.id.revenue_name_input);
        inputUtils.valueInput = revenuesFragmentView.findViewById(R.id.revenue_value_input);
        inputUtils.commentInput = revenuesFragmentView.findViewById(R.id.revenue_comments_input);
        inputUtils.categoryInput = revenuesFragmentView.findViewById(R.id.revenue_category_input);
        inputUtils.dateInput = revenuesFragmentView.findViewById(R.id.revenue_date_input);
        inputUtils.deleteButton = revenuesFragmentView.findViewById(R.id.revenue_delete_button);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        BudgetTypesViewModel viewModel = ViewModelProviders.of(transactionAddActivity).get(BudgetTypesViewModel.class);
        databaseUtils = new TransactionDatabaseHelper(getContext(), currentTime, inputUtils, budgetsTypes, viewModel, getString(R.string.revenues_section_key));

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        setUpDeleteButton();
    }

    private void setUpExpensesInputUtils(View expensesFragmentView) {
        inputUtils.nameInputField = expensesFragmentView.findViewById(R.id.expenses_name_field);
        inputUtils.valueInputField = expensesFragmentView.findViewById(R.id.expenses_value_field);
        inputUtils.categoryInputField = expensesFragmentView.findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = expensesFragmentView.findViewById(R.id.expenses_name_input);
        inputUtils.valueInput = expensesFragmentView.findViewById(R.id.expenses_value_input);
        inputUtils.commentInput = expensesFragmentView.findViewById(R.id.expenses_comments_input);
        inputUtils.categoryInput = expensesFragmentView.findViewById(R.id.expenses_category_input);
        inputUtils.dateInput = expensesFragmentView.findViewById(R.id.expenses_date_input);
        inputUtils.deleteButton = expensesFragmentView.findViewById(R.id.expense_delete_button);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        BudgetTypesViewModel viewModel = ViewModelProviders.of(transactionAddActivity).get(BudgetTypesViewModel.class);
        databaseUtils = new TransactionDatabaseHelper(getContext(), currentTime, inputUtils, budgetsTypes, viewModel, getString(R.string.expenses_section_key));

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        setUpDeleteButton();
    }

    private void setUpDeleteButton() {
        inputUtils.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private SaveDataCommunicator fromActivityCommunicator = new SaveDataCommunicator() {
        @Override
        public void message() {
            Log.d(TAG, "the message from activity was received");
            databaseUtils.insertOrUpdateData(true, false, null);
            databaseUtils.addTextListener();
        }
    };

    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputUtils.dateInput.setText(stringDate);
        }
    };
}