package com.example.searchwithdebounceandcancellation
val RECURSIVE_MENU_JSON = """
{
    "storeName": "Toast Kitchen",
    "menuGroups": [
    {
        "groupId": "g1",
        "groupName": "Main Course",
        "startingPrice": 15.00,
        "items": [
        { "id": "m1", "label": "Classic Burger", "cost": 12.50, "stock": 20 }
        ],
        "subGroups": []
    },
    {
        "groupId": "g2",
        "groupName": "Refreshments",
        "startingPrice": 3.00,
        "items": [],
        "subGroups": [
        {
            "groupId": "g3",
            "groupName": "Cold Brews",
            "startingPrice": 4.50,
            "items": [
            { "id": "m2", "label": "Iced Latte", "cost": null, "stock": 15 }
            ],
            "subGroups": [
            {
                "groupId": "g4",
                "groupName": "Flavored Syrups",
                "startingPrice": 0.50,
                "items": [
                { "id": "m3", "label": "Vanilla Bean Latte", "cost": 5.50, "stock": 10 }
                ],
                "subGroups": []
            }
            ]
        }
        ]
    }
    ]
}""".trimIndent()