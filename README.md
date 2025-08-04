Tonkotsu Anime App
Overview

The Tonkotsu Anime App is a modern Android application built with Jetpack Compose that allows users to explore detailed information about various anime titles. It focuses on providing a rich user experience with responsive UI, comprehensive data display, and interactive media galleries.
Features. Currently a work in progress.

This application provides a robust set of features to enhance the anime browsing experience:

    Detailed Anime Profiles: View extensive information for each anime, including titles (main, alternative, Japanese), scores, popularity ranks, and favorites.

    Comprehensive Synopsis & Background: Read full synopses and background stories with an expandable text feature for long descriptions.

    Categorization: Explore anime categorized by genres, themes, and other relevant categories.

    Production Details: See information about the studios, producers, and licensors involved in the anime's creation.

    Related Content Sections:

        Episodes List: Browse a list of episodes with their titles, air dates, and scores.

        Character List: Discover characters associated with the anime, including their roles and voice actors.

        Image Gallery: View a collection of high-resolution images related to the anime in a full-screen, swipeable gallery.

        Video Gallery: Watch trailers and other promotional videos in a full-screen, swipeable video player.

    External Links & Streaming Services: Access external links and find where to stream the anime directly from the app.

    Dynamic UI States: The app intelligently handles various UI states, including:

        Loading Indicators: Provides visual feedback while data is being fetched.

        Error Messages: Displays informative messages when data loading fails, with retry options.

        Empty States: Gracefully handles scenarios where no data is available for a section.

    Interactive Overlays: Tap on images or videos to open them in immersive full-screen overlays with intuitive navigation controls (arrows, swipe gestures) that fade in/out on interaction.

Technologies Used

    Kotlin: The primary programming language for Android development.

    Jetpack Compose: Android's modern toolkit for building native UI.

    AndroidX Libraries: A comprehensive set of libraries to help develop high-quality, robust, and compatible apps.

    Coil: A fast, lightweight image loading library for Android.

    MVVM Architecture: Utilizes the Model-View-ViewModel architectural pattern for clear separation of concerns, testability, and maintainability.

        UiState Sealed Class: A custom sealed class (UiState<T>) is used to manage and represent the various states of UI data (Loading, Success, Error) in a type-safe and explicit manner.

        ViewModel: Manages UI-related data and business logic, exposing StateFlows for UI observation.

        Use Cases (Interactors): Encapsulate specific business logic operations, making the ViewModel cleaner and more focused on UI state.

Project Structure Highlights

The project is structured to promote modularity and maintainability:

    core/common/UiState.kt: Defines the sealed UiState class for consistent UI state management across the application.

    core/domain/model/: Contains pure Kotlin data classes representing the application's domain entities (e.g., AnimeDetail, Image, Video, Episode, Character).

    core/domain/usecase/: Houses use cases (interactors) that define specific business operations.

    ui/composables/: Contains reusable UI components built with Jetpack Compose (e.g., ImageList, VideoList, CharacterListSection, FullScreenOverlay).

    ui/fullscreenoverlay/: Dedicated package for full-screen overlay components like ImageGalleryFullScreen and VideoGalleryFullScreen.

    feature/home/presentation/: Contains UI-related components and ViewModel for the home screen.

    animedetail/presentation/: Contains UI-related components and ViewModel for the anime detail screen.

License

(This section is a placeholder. You would typically add your project's license information here.)
