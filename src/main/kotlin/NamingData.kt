/** Data class to store naming data and make serialisation easier.
 *
 * @param count number of stored names.
 * @param data the data.
 */
data class NamingData(var count: Int, var data: HashMap<String, Int>)