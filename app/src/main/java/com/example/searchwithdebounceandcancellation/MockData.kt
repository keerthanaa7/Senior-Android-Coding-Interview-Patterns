package com.example.searchwithdebounceandcancellation

val TOAST_MENU_JSON = """
{
  "restaurantName": "Toast Cafe",
  "categories": [
    {
      "id": "c1",
      "name": "Pizza",
      "basePrice": 12.00,
      "items": [
        { "id": "i1", "name": "Cheese Pizza", "price": null, "inventory": 15 },
        { "id": "i2", "name": "Pepperoni Pizza", "price": 14.50, "inventory": 2 },
        { "id": "i3", "name": "Veggie Pizza", "price": null, "inventory": 4 }
      ]
    },
    {
      "id": "c2",
      "name": "Drinks",
      "basePrice": 2.50,
      "items": [
        { "id": "i4", "name": "Coke", "price": null, "inventory": 40 },
        { "id": "i5", "name": "Craft Beer", "price": 7.00, "inventory": 8 }
      ]
    }
  ]
}
""".trimIndent()