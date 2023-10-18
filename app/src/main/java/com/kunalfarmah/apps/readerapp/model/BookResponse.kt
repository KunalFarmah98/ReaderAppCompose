package com.kunalfarmah.apps.readerapp.model

data class BookResponse(
    val items: List<Item>,
    val kind: String?, // books#volumes
    val totalItems: Int? // 1584
) {
    data class Item(
        val accessInfo: AccessInfo?,
        val etag: String?, // +9i7+g6RzQ0
        val id: String?, // GmTtDwAAQBAJ
        val kind: String?, // books#volume
        val saleInfo: SaleInfo?,
        val searchInfo: SearchInfo?,
        val selfLink: String?, // https://www.googleapis.com/books/v1/volumes/GmTtDwAAQBAJ
        val volumeInfo: VolumeInfo?
    ) {
        data class AccessInfo(
            val accessViewStatus: String?, // SAMPLE
            val country: String?, // IN
            val embeddable: Boolean?, // true
            val epub: Epub?,
            val pdf: Pdf?,
            val publicDomain: Boolean?, // false
            val quoteSharingAllowed: Boolean?, // false
            val textToSpeechPermission: String?, // ALLOWED
            val viewability: String?, // PARTIAL
            val webReaderLink: String? // http://play.google.com/books/reader?id=GmTtDwAAQBAJ&hl=&source=gbs_api
        ) {
            data class Epub(
                val acsTokenLink: String?, // http://books.google.co.in/books/download/Beginning_App_Development_with_Flutter-sample-epub.acsm?id=I9TBDwAAQBAJ&format=epub&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api
                val isAvailable: Boolean? // false
            )

            data class Pdf(
                val acsTokenLink: String?, // http://books.google.co.in/books/download/Flutter_For_Dummies-sample-pdf.acsm?id=GmTtDwAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api
                val isAvailable: Boolean? // true
            )
        }

        data class SaleInfo(
            val buyLink: String?, // https://play.google.com/store/books/details?id=7qZJEAAAQBAJ&rdid=book-7qZJEAAAQBAJ&rdot=1&source=gbs_api
            val country: String?, // IN
            val isEbook: Boolean?, // false
            val listPrice: ListPrice?,
            val offers: List<Offer?>?,
            val retailPrice: RetailPrice?,
            val saleability: String? // NOT_FOR_SALE
        ) {
            data class ListPrice(
                val amount: Double?, // 3515.21
                val currencyCode: String? // INR
            )

            data class Offer(
                val finskyOfferType: Int?, // 1
                val listPrice: ListPrice?,
                val retailPrice: RetailPrice?
            ) {
                data class ListPrice(
                    val amountInMicros: Long?, // 3515210000
                    val currencyCode: String? // INR
                )

                data class RetailPrice(
                    val amountInMicros: Long?, // 3515210000
                    val currencyCode: String? // INR
                )
            }

            data class RetailPrice(
                val amount: Double?, // 3515.21
                val currencyCode: String? // INR
            )
        }

        data class SearchInfo(
            val textSnippet: String? // This book includes how to create an intuitive and stunning UI, add rich interactivity, and easily pull in data.
        )

        data class VolumeInfo(
            val allowAnonLogging: Boolean?, // false
            val authors: List<String?>?,
            val averageRating: Int?, // 5
            val canonicalVolumeLink: String?, // https://books.google.com/books/about/Flutter_For_Dummies.html?hl=&id=GmTtDwAAQBAJ
            val categories: List<String?>?,
            val contentVersion: String?, // 0.2.1.0.preview.1
            val description: String?, // Create awesome iOS and Android apps with a single tool! Flutter is an app developer’s dream come true. With Google’s open source toolkit, you can easily build beautiful apps that work across platforms using a single codebase. This flexibility allows you to get your work out to the widest possible audience. With Flutter already being used by thousands of developers worldwide in a market where billions of apps are downloaded every year, now is the right time to get ahead of the curve with this incredible tool. Flutter for Dummies is your friendly, ground-up route to creating multi-platform apps. From how to construct your initial frameworks to writing code in Dart, you’ll find the essentials you need to ride the Flutter revolutionary wave to success. This book includes how to create an intuitive and stunning UI, add rich interactivity, and easily pull in data. You’ll also see how Flutter features like Hot Reload—providing sub-second refreshes as you refine your work—help you make sure your app is a delight to use. Start simple: follow steps to build a basic app It’s alive! Keep connected to online data It moves! Make things fun with animated features Get the word out: use tips to expand your audience Whether you’re a fledgling developer or an expert wanting to add a slick feather to your programming cap, join the Flutter revolution now and soar above the rest!
            val imageLinks: ImageLinks?,
            val industryIdentifiers: List<IndustryIdentifier?>?,
            val infoLink: String?, // http://books.google.co.in/books?id=GmTtDwAAQBAJ&dq=flutter&hl=&source=gbs_api
            val language: String?, // en
            val maturityRating: String?, // NOT_MATURE
            val pageCount: Int?, // 386
            val panelizationSummary: PanelizationSummary?,
            val previewLink: String?, // http://books.google.co.in/books?id=GmTtDwAAQBAJ&printsec=frontcover&dq=flutter&hl=&cd=1&source=gbs_api
            val printType: String?, // BOOK
            val publishedDate: String?, // 2020-08-04
            val publisher: String?, // John Wiley & Sons
            val ratingsCount: Int?, // 1
            val readingModes: ReadingModes?,
            val subtitle: String?, // A Hands On Guide to App Development
            val title: String? // Flutter For Dummies
        ) {
            data class ImageLinks(
                val smallThumbnail: String?, // http://books.google.com/books/content?id=GmTtDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api
                val thumbnail: String? // http://books.google.com/books/content?id=GmTtDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api
            )

            data class IndustryIdentifier(
                val identifier: String?, // 9781119612582
                val type: String? // ISBN_13
            )

            data class PanelizationSummary(
                val containsEpubBubbles: Boolean?, // false
                val containsImageBubbles: Boolean? // false
            )

            data class ReadingModes(
                val image: Boolean?, // true
                val text: Boolean? // false
            )
        }
    }
}