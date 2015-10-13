/**
 * @author Swapnil
 */
import twitter4j._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.JsonAST._
import java.text.SimpleDateFormat
import java.util.SimpleTimeZone
import java.util.Properties
import java.nio._
import java.nio.file.StandardOpenOption
import java.nio.file.Paths
object TwitterStream {
	object Util {
		val config = new twitter4j.conf.ConfigurationBuilder()
		.setDebugEnabled(true)
		.setOAuthConsumerKey("icTkcqDwkRIBiWAejyHXo9eSe")
		.setOAuthConsumerSecret("kTjBkrMrQ6D4H2yQNByKYn1u9sOd8xLxk3GZE6i3ojSbjGjcut")
		.setOAuthAccessToken("3605652613-4vrcEgv867w199ivavT187oQ7mJtvyjZj2X1rqd")
		.setOAuthAccessTokenSecret("fMsH4jdt7XFIVfOoBJ2Ahqd3Sn4WwlPtaF2bGcPmv5Njg")
		.setIncludeEntitiesEnabled(true)
		.build

		def simpleStatusListener = new StatusListener() {
			def onStatus(status: Status) { 



				val format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:'Z'")
				format.setTimeZone(new SimpleTimeZone (SimpleTimeZone.UTC_TIME,"UTC"))
				var hashTag=status.getHashtagEntities
				var hashStr:String=""
				if (hashTag.length>0) {
					for(i <- 0 to hashTag.length-1)
					{
						//println(i)
						hashStr=hashStr+hashTag(i).getText
								//  println(hashTag(i).getText)

					}
				}

				hashStr=hashStr.replaceFirst(",", "")
						//	println("hash"+hashStr)



						val json =( ("datetime" -> format.format(status.getCreatedAt).toString()) ~ 
								("id" -> status.getId) ~
								("tweet" -> status.getText) ~
								("in_reply_to" ->status.getInReplyToStatusId) ~
								("user" ->status.getUser.getName) ~
								("pref_user_name" ->status.getUser.getScreenName) ~
								("verified" ->status.isPossiblySensitive()) ~
								("followerscount" ->status.getUser.getFavouritesCount) ~
								("friendscount" ->status.getUser.getFriendsCount) ~
								("statusescount" ->status.getUser.getStatusesCount) ~
								("fav_count" ->status.isFavorited()) ~
								("profile_image_url" ->status.getUser.getProfileImageURL) ~
								("retweetcount" ->status.getRetweetCount) ~
								("lang" ->status.getLang) ~
								("hashtags" ->hashStr) ~
								("trends" ->"") 
								)
                 println("writting msg")
                  java.nio.file.Files.write(Paths.get("C:\\twitterfiles\\tweets.txt"), compact(render(json)).getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
								println(compact(render(json))) 
               
							


			}
			def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
			def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
			def onException(ex: Exception) { ex.printStackTrace }
			def onScrubGeo(arg0: Long, arg1: Long) {}
			def onStallWarning(warning: StallWarning) {}
		}
	}

	def main(args: Array[String]) {

    println("test")
    val filter=List("barclays","barclaycard","morgan stanley","santander")
		val twitterStream = new TwitterStreamFactory(Util.config).getInstance
				twitterStream.addListener(Util.simpleStatusListener)
				twitterStream.filter(new FilterQuery().track(filter.mkString(",")))
				//Thread.sleep(10000)
				// twitterStream.cleanUp
				//twitterStream.shutdown
	}
}