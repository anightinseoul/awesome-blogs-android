package org.petabytes.awesomeblogs.feeds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.petabytes.api.model.Entry;
import org.petabytes.awesomeblogs.R;
import org.petabytes.awesomeblogs.web.WebViewActivity;
import org.petabytes.awesomeblogs.util.Dates;
import org.petabytes.coordinator.Coordinator;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

class EntryGradientCoordinator extends Coordinator {

    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.author) TextView authorView;
    @BindView(R.id.summary) TextView summaryView;

    private final Context context;
    private final Entry entry;

    EntryGradientCoordinator(@NonNull Context context, @NonNull Entry entry) {
        this.context = context;
        this.entry = entry;
    }

    @Override
    public void attach(@NonNull View view) {
        super.attach(view);
        switch (new Random().nextInt(3)) {
            case 0: view.setBackgroundResource(R.drawable.background_gradient_0); break;
            case 1: view.setBackgroundResource(R.drawable.background_gradient_1); break;
            case 2: view.setBackgroundResource(R.drawable.background_gradient_2); break;
        }
        titleView.setText(entry.getTitle());
        authorView.setText("by " + entry.getAuthor() + "  /  " + Dates.getRelativeTimeString(
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                .parse(entry.getUpdatedAt(), new ParsePosition(0)).getTime()));

        bind(Observable.just(entry.getSummary().trim())
            .map(summary -> Jsoup.parse(summary).text())
            .subscribeOn(Schedulers.io()), summary -> summaryView.setText(summary));
    }

    @OnClick(R.id.container)
    void onContainerClick() {
        context.startActivity(WebViewActivity.intent(context, entry.getLink()));
    }
}