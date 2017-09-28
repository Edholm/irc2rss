package pub.edholm.irc2rss.domain

class CategoryCoverter {
  companion object {
    private val tlCategoryMap = mapOf(
      "Animation :: Anime" to Category.ANIME,
      "Applications :: Mac" to Category.APPLICATIONS_MAC,
      "Applications :: PC - ISO" to Category.APPLICATIONS_PC,
      "Books :: Comics" to Category.BOOKS_COMICS,
      "Games :: PC" to Category.GAMES_PC,
      "Games :: PS4" to Category.GAMES_PS4,
      "Movies :: 4K" to Category.MOVIES_4K,
      "Movies :: Bluray" to Category.MOVIES_BLURAY,
      "Movies :: BlurayRip" to Category.MOVIES_BLURAY_RIP,
      "Movies :: Documentaries" to Category.MOVIES_DOCUMENTARIES,
      "Movies :: DVDRip/DVDScreener" to Category.MOVIES_DVDRIP,
      "Movies :: Foreign" to Category.MOVIES_FOREIGN,
      "Movies :: WEBRip" to Category.MOVIES_WEBRIP,
      "Music :: Music Videos" to Category.MUSIC_VIDEOS,
      "TV :: BoxSets" to Category.TV_BOXSETS,
      "TV :: Episodes" to Category.TV_EPISODES,
      "TV :: Episodes HD" to Category.TV_EPISODES_HD,
      "TV :: Foreign" to Category.TV_FOREIGN
    )

    fun fromTorrentLeech(tlCategory: String) = tlCategoryMap[tlCategory] ?: Category.UNKNOWN
  }
}