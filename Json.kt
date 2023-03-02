class Json {
     companion object {
         // A regex to retrieve each element of an array
         private val regexArray = Regex(""" *(\{[^}]+}|\[[^]]+]|[^,}]+)""")
         // A regex to retrieve the key and value of a key-value object
         private val regex = Regex(""""([\w\s-]*)": *(\{[^}]+}|\[[^]]+]|[^,}]+)""")

         /***
          * @private
          * Convert a string into several types (e.g. Int, Array, Map, ...)
          *
          * @param data The value to convert
          * @return The converted value
          */
         private fun whenData(data: String): Any {
             return when {
                 data == "true" -> true
                 data == "false" -> false
                 data.toIntOrNull() != null -> data.toInt()
                 data.toLongOrNull() != null -> data.toLong()
                 data.toFloatOrNull() != null -> data.toFloat()
                 data.toShortOrNull() != null -> data.toShort()
                 data.toByteOrNull() != null -> data.toByte()
                 data.startsWith('"') -> data.subSequence(1, data.length-1)
                 data.startsWith("{") -> this.objectKeyValue(data)
                 data.startsWith("[") -> this.objectIndexValue(data)
                 else -> data
             }
         }

         /***
          * @private
          * parse a stringify object index-value and transform it into a map
          *
          * @param stringify A stringify object index-value
          * @return A array
          */
         private fun objectIndexValue(stringify: String): Array<Any> {
             var array = arrayOf<Any>()
             val match = regexArray.findAll(stringify.subSequence(1, stringify.length-1))
             for (matchResult in match) {
                 val data: List<String> = matchResult.destructured.toList()
                 array += this.whenData(data[0])
             }
             return array
         }

         /***
          * @private
          * parse a stringify object key-value and transform it into a map
          *
          * @param stringify A stringify object key-value
          * @return A map
          */
         private fun objectKeyValue(stringify: String): MutableMap<String, Any> {
             val map = mutableMapOf<String, Any>()
             val match = regex.findAll(stringify)
             for (matchResult in match) {
                 val data: List<String> = matchResult.destructured.toList()
                 map[data[0]] = this.whenData(data[1])
             }

             return map
         }

         /***
          * parse a stringify json and transform it into a map
          *
          * @param stringify The stringify json
          * @return The Map
          */
         fun parse(stringify: String): MutableMap<String, Any> {
             return objectKeyValue(stringify)
         }
     }
}
