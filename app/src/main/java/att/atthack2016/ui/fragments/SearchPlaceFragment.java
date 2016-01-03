package att.atthack2016.ui.fragments;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import att.atthack2016.R;
import att.atthack2016.model.PlacePrediction;
import att.atthack2016.ui.activities.HomeActivity;
import att.atthack2016.ui.adapters.SearchResultsAdapter;
import att.atthack2016.ui.adapters.interfaces.OnItemClickListener;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Edgar Salvador Maurilio on 02/01/2016.
 */
public class SearchPlaceFragment extends Fragment implements OnItemClickListener<PlacePrediction> {

    @Bind(R.id.list_places)
    RecyclerView mResultsList;
    SearchResultsAdapter mResultsAdapter;

    AppCompatEditText searchInput;


    public SearchPlaceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_place, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchToolbar();
        setupSearchResultsList();
    }

    public void setupSearchToolbar() {
        Toolbar toolbar = changeColorWithTransition(getToolbar(), false);
        searchInput = buildSearchInput();

        toolbar.addView(searchInput, new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public Toolbar getToolbar() {
        return ((HomeActivity) getActivity()).getToolbar();
    }


    private AppCompatEditText buildSearchInput() {
        AppCompatEditText searchInput = new AppCompatEditText(getContext());
        searchInput.setHint(R.string.hint_location_search);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onQueryChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return searchInput;
    }


    public void onQueryChanged(CharSequence query) {
        if (query.length() >= 3) {
            //Search
//            mSearchPlacePresenter.searchPlaces(query.toString());
        } else if (query.length() <= 2)
            mResultsAdapter.clear();
    }


    private Toolbar changeColorWithTransition(Toolbar toolbar, boolean isReverse) {
        TransitionDrawable colorTransition = (TransitionDrawable) toolbar.getBackground();

        if (!isReverse)
            colorTransition.startTransition(300);
        else
            colorTransition.reverseTransition(300);

        return toolbar;
    }


    private void setupSearchResultsList() {
        mResultsAdapter = buildSearchResultsAdapter(this);
        mResultsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mResultsList.setAdapter(mResultsAdapter);
    }


    private SearchResultsAdapter buildSearchResultsAdapter(OnItemClickListener<PlacePrediction> listener) {
        SearchResultsAdapter resultsAdapter = new SearchResultsAdapter(getContext());
        resultsAdapter.setOnItemClickListener(listener);
        return resultsAdapter;
    }

    @Override
    public void onItemClicked(PlacePrediction placePrediction) {
        //Click en los resultados XD
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeSearchInputFromToolbar();
    }

    public void removeSearchInputFromToolbar() {
        Toolbar toolbar = changeColorWithTransition(getToolbar(), true);
        toolbar.removeView(searchInput);
    }
}
