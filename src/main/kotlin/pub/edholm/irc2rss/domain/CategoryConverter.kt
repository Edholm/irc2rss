package pub.edholm.irc2rss.domain

class CategoryConverter {
  companion object {
    private val tlCategoryMap = mapOf(
      "Animation :: Anime" to Category.ANIME,
      "Applications :: Mac" to Category.APPLICATIONS_MAC,
      "Applications :: PC - ISO" to Category.APPLICATIONS_PC,
      "Applications :: 0-day" to Category.APPLICATIONS_0DAY,
      "Books :: Comics" to Category.BOOKS_COMICS,
      "Books :: EBooks" to Category.BOOKS_EBOOKS,
      "Games :: PC" to Category.GAMES_PC,
      "Games :: PS4" to Category.GAMES_PS4,
      "Games :: Wii" to Category.GAMES_WII,
      "Games :: Nintendo DS" to Category.GAMES_NINTENDO_DS,
      "Games :: Mac" to Category.GAMES_MAC,
      "Movies :: 4K" to Category.MOVIES_4K,
      "Movies :: Real 4K UltraHD HDR" to Category.MOVIES_REAL_4K_ULTRAHD_HDR,
      "Movies :: 4K Upscaled/UHD LQ" to Category.MOVIES_4K_UPSCALED,
      "Movies :: Bluray" to Category.MOVIES_BLURAY,
      "Movies :: BlurayRip" to Category.MOVIES_BLURAY_RIP,
      "Movies :: Documentaries" to Category.MOVIES_DOCUMENTARIES,
      "Movies :: DVDRip/DVDScreener" to Category.MOVIES_DVDRIP,
      "Movies :: Foreign" to Category.MOVIES_FOREIGN,
      "Movies :: WEBRip" to Category.MOVIES_WEBRIP,
      "Movies :: DVD-R" to Category.MOVIES_DVDR,
      "Movies :: HDRip" to Category.MOVIES_HDRIP,
      "Movies :: Boxsets" to Category.MOVIES_BOXSETS,
      "Music :: Music Videos" to Category.MUSIC_VIDEOS,
      "TV :: BoxSets" to Category.TV_BOXSETS,
      "TV :: Episodes" to Category.TV_EPISODES,
      "TV :: Episodes HD" to Category.TV_EPISODES_HD,
      "TV :: Foreign" to Category.TV_FOREIGN
    )

    fun fromTorrentLeech(tlCategory: String) = tlCategoryMap[tlCategory] ?: Category.UNKNOWN
  }
}