package ca.etsmtl.applets.repository.data.model

import java.util.Date

/**
 * Created by Sonphil on 18-01-19.
 */

data class News (
    var link: String,
    var title: String,
    var description: String,
    var updateDate: Date,
    var imageUrl: String,
    var pubDate: Date
)