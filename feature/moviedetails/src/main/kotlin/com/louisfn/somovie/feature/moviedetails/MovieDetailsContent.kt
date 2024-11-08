package com.louisfn.somovie.feature.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.louisfn.somovie.domain.model.Actor
import com.louisfn.somovie.domain.model.MovieGenre
import com.louisfn.somovie.domain.model.YoutubeVideo
import com.louisfn.somovie.ui.common.LocalAppRouter
import com.louisfn.somovie.ui.common.R
import com.louisfn.somovie.ui.common.extension.bottom
import com.louisfn.somovie.ui.common.extension.rememberVectorPainter
import com.louisfn.somovie.ui.common.extension.start
import com.louisfn.somovie.ui.common.extension.toRuntimeString
import com.louisfn.somovie.ui.common.model.ImmutableList
import com.louisfn.somovie.ui.component.AutosizeText
import com.louisfn.somovie.ui.component.DefaultAsyncImage
import com.louisfn.somovie.ui.component.DefaultTextButton
import com.louisfn.somovie.ui.theme.AppColor
import com.louisfn.somovie.ui.theme.Dimens

private const val OverviewCollapsedMaxLines = 4
private val MoreInfoSectionSpace = 16.dp
private val MoreInfoSectionBodySpace = 8.dp

@Composable
internal fun MovieDetailsScreen(
    state: ContentUiState.Content,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .navigationBarsPadding()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = Dimens.DefaultScreenHorizontalPadding,
                vertical = Dimens.DefaultScreenVerticalPadding,
            ),
    ) {
        OverviewSection(overview = state.overview)
        GenresFlowSection(genres = state.genres)
        MoreInfoSection(state = state)
        CastSection(cast = state.cast)
        VideosSection(videos = state.videos)
    }
}

@Composable
private fun SectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
    )
}

@Composable
private fun ColumnScope.OverviewSection(overview: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(true) }

    SectionText(text = stringResource(id = R.string.movie_details_about_section))
    Spacer(modifier = Modifier.height(MoreInfoSectionBodySpace))

    Text(
        text = overview,
        maxLines = if (isExpanded) Int.MAX_VALUE else OverviewCollapsedMaxLines,
        style = MaterialTheme.typography.body2,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { isButtonVisible = isExpanded || it.hasVisualOverflow },
    )

    if (isButtonVisible) {
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentEnforcement provides false,
        ) {
            DefaultTextButton(
                text = stringResource(
                    id = if (isExpanded) {
                        R.string.movie_details_about_read_less
                    } else {
                        R.string.movie_details_about_read_more
                    },
                ),
                modifier = Modifier
                    .padding(0.dp)
                    .offset(x = -ButtonDefaults.TextButtonContentPadding.start),
                onClick = { isExpanded = !isExpanded },
            )
        }
    }

    Spacer(modifier = Modifier.height(MoreInfoSectionSpace - ButtonDefaults.TextButtonContentPadding.bottom))
}

@Composable
private fun ColumnScope.GenresFlowSection(genres: ImmutableList<MovieGenre>?) {
    genres ?: return

    SectionText(text = stringResource(id = R.string.movie_details_genres_section))
    Spacer(modifier = Modifier.height(MoreInfoSectionBodySpace))
    FlowRow {
        genres.forEach { genre ->
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 12.dp),
                text = genre.name,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface,
            )
        }
    }
    Spacer(modifier = Modifier.height(MoreInfoSectionSpace))
}

@Composable
private fun ColumnScope.MoreInfoSection(state: ContentUiState.Content) {
    SectionText(text = stringResource(id = R.string.movie_details_movie_info_section))
    Spacer(modifier = Modifier.height(MoreInfoSectionBodySpace))
    MoreInfoRowItem(
        title = stringResource(id = R.string.movie_details_movie_info_original_title),
        value = state.originalTitle,
    )
    MoreInfoRowItem(
        title = stringResource(id = R.string.movie_details_movie_info_original_language),
        value = state.originalLanguage,
    )
    state.runtime?.let {
        MoreInfoRowItem(
            title = stringResource(id = R.string.movie_details_movie_info_runtime),
            value = it.toRuntimeString(LocalContext.current),
        )
    }
    MoreInfoRowItem(
        title = stringResource(id = R.string.movie_details_movie_info_budget),
        value = state.budget,
    )
    MoreInfoRowItem(
        title = stringResource(id = R.string.movie_details_movie_info_revenue),
        value = state.revenue,
    )
    Spacer(modifier = Modifier.height(MoreInfoSectionSpace))
}

@Composable
private fun MoreInfoRowItem(
    title: String,
    value: String,
) {
    MoreInfoRowItem {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
        )
        AutosizeText(
            text = value,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2,
        )
    }
}

@Composable
private fun MoreInfoRowItem(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content,
    )
}

@Composable
private fun ColumnScope.CastSection(cast: ImmutableList<Actor>?) {
    cast ?: return

    val placeHolder =
        rememberVectorPainter(image = Icons.Sharp.AccountCircle, tintColor = AppColor.Gray)

    SectionText(text = stringResource(id = R.string.movie_details_movie_cast_section))
    Spacer(modifier = Modifier.height(MoreInfoSectionBodySpace))
    LazyRow(
        modifier = Modifier
            .requiredWidth(LocalConfiguration.current.screenWidthDp.dp),
    ) {
        items(
            items = cast,
            key = { it.id },
        ) { item ->
            Column(
                modifier = Modifier
                    .width(96.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefaultAsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    model = item.profilePath,
                    contentScale = ContentScale.Crop,
                    placeholder = placeHolder,
                    error = placeHolder,
                )
                Text(
                    text = item.character,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(MoreInfoSectionSpace))
}

@Composable
private fun ColumnScope.VideosSection(videos: ImmutableList<YoutubeVideo>?) {
    videos ?: return

    SectionText(text = stringResource(id = R.string.movie_details_movie_videos_section))
    Spacer(modifier = Modifier.height(MoreInfoSectionBodySpace))
    LazyRow(
        Modifier
            .requiredWidth(LocalConfiguration.current.screenWidthDp.dp),
        contentPadding = PaddingValues(horizontal = Dimens.DefaultScreenHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(videos) { VideoItem(it) }
    }
    Spacer(modifier = Modifier.height(MoreInfoSectionSpace))
}

@Composable
private fun VideoItem(video: YoutubeVideo) {
    val router = LocalAppRouter.current

    Column(
        modifier = Modifier
            .width(180.dp)
            .clickable { router.openYoutubeVideo(video.key) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DefaultAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(Dimens.YoutubeThumbnailRatio)
                .clip(MaterialTheme.shapes.medium),
            model = video.thumbnailUrl,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            textAlign = TextAlign.Center,
            maxLines = 2,
            text = video.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
        )
    }
}
