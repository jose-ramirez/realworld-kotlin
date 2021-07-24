package dev.josers.realworld

class RestMethods {
    class Users {
        class V1 {
            companion object {
                const val USER = "/api/user"
                const val PATH = "/api/users"
                const val LOGIN = "$PATH/login"
            }
        }
    }

    class Profiles {
        class V1 {
            companion object {
                const val PATH = "/api/profiles"
                const val PROFILE = "$PATH/{username}"
                const val PROFILE_FOLLOW = "$PATH/{username}/follow"
            }
        }
    }

    class Articles {
        class V1 {
            companion object {
                const val PATH = "/api/articles"
                const val FEED = "/api/articles/feed"
                const val ARTICLES_BY_SLUG = "$PATH/{slug}"
            }
        }
    }

    class Tags {
        class V1 {
            companion object {
                const val PATH = "/api/tags"
            }
        }
    }
}