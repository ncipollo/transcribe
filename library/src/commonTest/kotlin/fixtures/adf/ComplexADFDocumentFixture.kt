package fixtures.adf

object ComplexADFDocumentFixture {
    const val COMPLEX_DOCUMENT = """
{
    "type": "doc",
    "content": [
        {
            "type": "paragraph",
            "attrs": {
                "localId": "ebce2b5a-9522-46e3-833a-f065e40d688d"
            },
            "content": [
                {
                    "text": "This content exists outside of a section.",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "be1273c5-79c0-4edb-8c28-1b85b9cd248b"
            },
            "content": [
                {
                    "text": "Section 1",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "95409dc3-9ea4-4e1a-8a88-52a035cc73d0"
            },
            "content": [
                {
                    "text": "This is normal paragraph text. Nothing interesting here.",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "be3232db-826c-4765-a123-83590afe5f2f"
            },
            "content": [
                {
                    "text": "Section 2",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "e9c77edd-49a8-4450-9b58-6e04b6ea59f4"
            },
            "content": [
                {
                    "text": "The following are bullets:",
                    "type": "text"
                }
            ]
        },
        {
            "type": "bulletList",
            "attrs": {
                "localId": "2d44bf4a-b86b-48e5-895d-54d1bee7f99a"
            },
            "content": [
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "eab2c106-72c4-486a-8d2f-7562b43c6dfc"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "954f7339-948a-4a96-9f22-c7cc73202723"
                            },
                            "content": [
                                {
                                    "text": "Bullet 1",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "a906f63c-3778-45bf-b9f0-121f6f4a502d"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "09830b5d-2535-45b9-8125-bee7cc82e69e"
                            },
                            "content": [
                                {
                                    "text": "Bullet 2",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "08b6caa8-f214-4ff0-9031-a9440c18115a"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "7ead1af5-5d00-4d75-8d6f-741dc83c03f1"
                            },
                            "content": [
                                {
                                    "text": "Bullet 3",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "45ca8dfd-eb27-41e9-bd02-14bf892aaa66"
            }
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "783cd8c4-4adf-4a6a-8b94-44328b9e0d97"
            },
            "content": [
                {
                    "text": "And here we have numeric bullets:",
                    "type": "text"
                }
            ]
        },
        {
            "type": "orderedList",
            "attrs": {
                "localId": "1614c39e-ff99-45e7-85b5-bb50dde27918",
                "order": 1
            },
            "content": [
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "07745610-7a06-4c37-af45-17b5ccf430fe"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "9137c7c1-a085-4cbf-b416-e68116f7250f"
                            },
                            "content": [
                                {
                                    "text": "The number one.",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "970b0466-0bb3-4f9c-a1f8-6c68bd0c3953"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "d223b931-2ea3-4ce4-801b-999a2463e4e0"
                            },
                            "content": [
                                {
                                    "text": "The number two",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "8b793512-ecc1-41c0-b654-997c325e9ecc"
            }
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "3834f4b5-6d70-41f9-a454-d72de4235ff9"
            },
            "content": [
                {
                    "text": "And here's a task list:",
                    "type": "text"
                }
            ]
        },
        {
            "type": "taskList",
            "attrs": {
                "localId": "727c0ad8-0f7a-43e0-b844-fa0709505e90"
            },
            "content": [
                {
                    "type": "taskItem",
                    "attrs": {
                        "state": "TODO",
                        "localId": "727b5745-dcf8-4c99-ad83-9b9c2a67c22d"
                    },
                    "content": [
                        {
                            "text": "Action 1",
                            "type": "text"
                        }
                    ]
                },
                {
                    "type": "taskItem",
                    "attrs": {
                        "state": "TODO",
                        "localId": "442d569b-720a-483e-b04a-afa6c256cd3c"
                    },
                    "content": [
                        {
                            "text": "Action 2",
                            "type": "text"
                        }
                    ]
                },
                {
                    "type": "taskItem",
                    "attrs": {
                        "state": "DONE",
                        "localId": "b4542707-b290-444b-8257-b912e2a73163"
                    },
                    "content": [
                        {
                            "text": "Checked",
                            "type": "text"
                        }
                    ]
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "e75710d4-c928-4443-a916-393ff34a90b1"
            },
            "content": [
                {
                    "text": "Section 3",
                    "type": "text"
                }
            ]
        },
        {
            "type": "table",
            "attrs": {
                "layout": "default",
                "width": 760,
                "localId": "ace030f9-16f0-43ec-b16f-40fff51ac499"
            },
            "content": [
                {
                    "type": "tableRow",
                    "attrs": {
                        "localId": "51582f70-f0de-4c90-9dca-99868e146c2a"
                    },
                    "content": [
                        {
                            "type": "tableHeader",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "a8679ff7-6dfc-4862-8202-6eb05ab23794"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "a88214fa-aeef-46aa-989e-4f56f4938e2c"
                                    },
                                    "content": [
                                        {
                                            "text": "Column 1",
                                            "type": "text",
                                            "marks": [
                                                {
                                                    "type": "strong"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableHeader",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "4ef0541f-ceba-4edc-bcdd-50652b0888dc"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "950349d5-f325-4ed9-85d5-e73e98007e5f"
                                    },
                                    "content": [
                                        {
                                            "text": "Column 2",
                                            "type": "text",
                                            "marks": [
                                                {
                                                    "type": "strong"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableHeader",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "db67663d-56f1-46cb-9207-8ab136cfca7b"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "8c8ecdaa-2df5-48c0-bcba-e49c70c974fd"
                                    },
                                    "content": [
                                        {
                                            "text": "Column 3",
                                            "type": "text",
                                            "marks": [
                                                {
                                                    "type": "strong"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "tableRow",
                    "attrs": {
                        "localId": "c8d3860a-2f92-4ace-a8e1-d340cd2af4f0"
                    },
                    "content": [
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "79e38ca6-1b23-441c-934c-d60326cd7f96"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "39a3ef37-55e6-43f7-a053-3b125ee1e1aa"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 1, Column 1",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "a1681ee5-0a9b-4c42-b983-d652d8b5d56f"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "197930e7-0357-4291-9c81-41a06625eada"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 1, Column 2",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "d8a1e90d-83c6-447c-afb7-321d84b1b537"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "d2dce7e8-1f1a-46c7-a69e-4bc279356f35"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 1, Column 3",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "tableRow",
                    "attrs": {
                        "localId": "431a2362-814f-43f7-bbfc-5164c1eefd53"
                    },
                    "content": [
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "1c315de0-8c27-435e-a83f-c9ea584dfae4"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "fdbf71c8-9785-4588-b384-4adf56b03d21"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 2, Column 1",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "b29ae780-48ec-4f68-9683-e0c3bba7d509"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "a5d56f69-e4f4-4efc-8b5c-08af1a631521"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 2, Column 2",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "tableCell",
                            "attrs": {
                                "colspan": 1,
                                "rowspan": 1,
                                "localId": "0c36b9eb-5d5e-4e22-abe7-b159fa362ce4"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "d51c9890-ad8b-4502-a41e-d731dd627c5a"
                                    },
                                    "content": [
                                        {
                                            "text": "Row 2, Column 3",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "b8700d6e-c27b-4126-a4b9-7ceefc1441fa"
            }
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "520a1da8-3742-4cdc-9b9a-5d0ae6aafba7"
            },
            "content": [
                {
                    "text": "Section 4",
                    "type": "text"
                }
            ]
        },
        {
            "type": "mediaSingle",
            "attrs": {
                "layout": "center",
                "width": 442,
                "widthType": "pixel"
            },
            "content": [
                {
                    "type": "media",
                    "attrs": {
                        "width": 1024,
                        "alt": "Tony",
                        "id": "8dfdd993-f45f-48ea-bde6-ac89319cbc37",
                        "collection": "contentId-5385126043",
                        "type": "file",
                        "localId": "06465c18-a12e-43bf-9809-08bb1bf595d2",
                        "height": 1536
                    }
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "7a60381a-088f-4a95-aa30-5e10c546ae38"
            },
            "content": [
                {
                    "text": "Section 5",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "034d26b0-9de4-409f-90cd-764a03757dfd"
            },
            "content": [
                {
                    "text": "This will have subsections.",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 2,
                "localId": "de8a80eb-0ce7-4380-bf15-294d1b09c5ba"
            },
            "content": [
                {
                    "text": "Sub 1",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 3,
                "localId": "2e74fe0d-00cd-4118-9cb4-0af600fc41ea"
            },
            "content": [
                {
                    "text": "H3 Section",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "50395dbb-1c3d-4ef3-b716-a43a24920c4c"
            },
            "content": [
                {
                    "text": "Wow, I'm nested!",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "74564a46-43c6-4b39-91e2-a0dad9027c0f"
            },
            "content": [
                {
                    "text": "Sub 2",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "ed7d6ed0-f0e2-429c-a0b6-187978a62ac8"
            },
            "content": [
                {
                    "text": "Subsection 2.",
                    "type": "text"
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "f769cbd0-dd58-4634-a0e5-7c94fb404b41"
            },
            "content": [
                {
                    "text": "Section 6",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "f8a0ccc8-8dbf-45ba-9118-aa21ede8d4b9"
            },
            "content": [
                {
                    "text": "Lots ",
                    "type": "text",
                    "marks": [
                        {
                            "type": "strong"
                        }
                    ]
                },
                {
                    "text": "of ",
                    "type": "text"
                },
                {
                    "text": "random",
                    "type": "text",
                    "marks": [
                        {
                            "type": "em"
                        }
                    ]
                },
                {
                    "text": " elements.",
                    "type": "text"
                }
            ]
        },
        {
            "type": "codeBlock",
            "attrs": {
                "language": "swift",
                "localId": "1a7bc7ab-a7c5-4716-abb5-301837c37ca3"
            },
            "content": [
                {
                    "text": "let thing = MyThing()",
                    "type": "text"
                }
            ]
        },
        {
            "type": "expand",
            "attrs": {
                "title": "Collapse",
                "localId": "a6f8b8b1-de19-4669-a51b-9bf0d6e26b57"
            },
            "content": [
                {
                    "type": "bulletList",
                    "attrs": {
                        "localId": "0f9da3a4-289e-4f5f-86ab-274e408babe5"
                    },
                    "content": [
                        {
                            "type": "listItem",
                            "attrs": {
                                "localId": "56a03c79-d785-4edc-a1a7-ed23516f09bc"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "e9165657-5610-4d68-8eff-f6b2255629b7"
                                    },
                                    "content": [
                                        {
                                            "text": "Content which is kinda hidden.",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "bulletList",
            "attrs": {
                "localId": "47619eab-fe6b-4582-baa4-814fde7568c0"
            },
            "content": [
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "4ce629fc-634f-49e1-b515-c3ed9e7b32c6"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "1741a034-0db2-427e-99b3-e6752ccd97f2"
                            },
                            "content": [
                                {
                                    "type": "status",
                                    "attrs": {
                                        "color": "blue",
                                        "style": "bold",
                                        "text": "Status 1",
                                        "localId": "5a1b494d-8033-4d64-9ac1-e235b5f76346"
                                    }
                                },
                                {
                                    "text": " ",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "listItem",
                    "attrs": {
                        "localId": "8da67aae-4141-4734-b7db-91b6a683c613"
                    },
                    "content": [
                        {
                            "type": "paragraph",
                            "attrs": {
                                "localId": "bc0f5d91-932c-4b1c-abce-e0d47ea658c5"
                            },
                            "content": [
                                {
                                    "type": "status",
                                    "attrs": {
                                        "color": "red",
                                        "style": "bold",
                                        "text": "Status 2",
                                        "localId": "6c2f3a46-3bd9-47d7-a727-ecb99334fd97"
                                    }
                                },
                                {
                                    "text": " ",
                                    "type": "text"
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "d40d800e-fcc1-412e-b3af-5dd41365325f"
            },
            "content": [
                {
                    "text": "Section 7",
                    "type": "text"
                }
            ]
        },
        {
            "type": "extension",
            "attrs": {
                "layout": "default",
                "extensionType": "com.atlassian.confluence.macro.core",
                "extensionKey": "drawio",
                "parameters": {
                    "macroParams": {
                        "mVer": {
                            "value": "2"
                        },
                        "zoom": {
                            "value": "1"
                        },
                        "simple": {
                            "value": "0"
                        },
                        "inComment": {
                            "value": "0"
                        },
                        "custContentId": {
                            "value": "5384339671"
                        },
                        "pageId": {
                            "value": "5385126043"
                        },
                        "lbox": {
                            "value": "1"
                        },
                        "diagramDisplayName": {
                            "value": "Untitled Diagram-1766526257160.drawio"
                        },
                        "contentVer": {
                            "value": "1"
                        },
                        "revision": {
                            "value": "1"
                        },
                        "baseUrl": {
                            "value": "https://whoopinc.atlassian.net/wiki"
                        },
                        "diagramName": {
                            "value": "Untitled Diagram-1766526257160.drawio"
                        },
                        "pCenter": {
                            "value": "0"
                        },
                        "width": {
                            "value": "281"
                        },
                        "links": {
                            "value": ""
                        },
                        "tbstyle": {
                            "value": ""
                        },
                        "height": {
                            "value": "391"
                        }
                    },
                    "macroMetadata": {
                        "macroId": {
                            "value": "7462dfdb-9a9b-4541-90e1-e5225c55efeb"
                        },
                        "schemaVersion": {
                            "value": "1"
                        },
                        "placeholder": [
                            {
                                "type": "image",
                                "data": {
                                    "url": "https://ac.draw.io/connectImage?mVer=2&zoom=1&simple=0&inComment=0&custContentId=5384339671&pageId=5385126043&lbox=1&diagramDisplayName=Untitled+Diagram-1766526257160.drawio&contentVer=1&revision=1&baseUrl=https%3A%2F%2Fwhoopinc.atlassian.net%2Fwiki&diagramName=Untitled+Diagram-1766526257160.drawio&pCenter=0&width=281&links=&tbstyle=&height=391"
                                }
                            }
                        ],
                        "title": "draw.io Diagram"
                    }
                },
                "localId": "c249b3ff-7dce-4308-8f7f-5190d6b92117"
            }
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "c50b9ac9-3eda-4c6b-a3c0-da56ebc4e442"
            },
            "content": [
                {
                    "text": "Section 8",
                    "type": "text"
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "ad85f743-ab25-4b37-9625-343db341c3ea"
            },
            "content": [
                {
                    "text": "This text has ",
                    "type": "text",
                    "marks": [
                        {
                            "type": "em"
                        }
                    ]
                },
                {
                    "text": "lots",
                    "type": "text",
                    "marks": [
                        {
                            "type": "code"
                        }
                    ]
                },
                {
                    "text": " of ",
                    "type": "text",
                    "marks": [
                        {
                            "type": "em"
                        }
                    ]
                },
                {
                    "text": "marks",
                    "type": "text",
                    "marks": [
                        {
                            "type": "strong"
                        },
                        {
                            "type": "em"
                        }
                    ]
                },
                {
                    "text": " applied to it.",
                    "type": "text",
                    "marks": [
                        {
                            "type": "em"
                        }
                    ]
                }
            ]
        },
        {
            "type": "heading",
            "attrs": {
                "level": 1,
                "localId": "7a073a54-6f2f-467a-9274-ea9157545645"
            },
            "content": [
                {
                    "text": "Section 9",
                    "type": "text"
                }
            ]
        },
        {
            "type": "blockquote",
            "attrs": {
                "localId": "6ec75cda-e5ec-4ce6-b6b8-c70797153898"
            },
            "content": [
                {
                    "type": "paragraph",
                    "attrs": {
                        "localId": "0329441f-c037-4251-a15e-942e558e1175"
                    },
                    "content": [
                        {
                            "text": "This",
                            "type": "text",
                            "marks": [
                                {
                                    "type": "strong"
                                }
                            ]
                        },
                        {
                            "text": " and ",
                            "type": "text"
                        },
                        {
                            "text": "that.",
                            "type": "text",
                            "marks": [
                                {
                                    "type": "em"
                                }
                            ]
                        }
                    ]
                },
                {
                    "type": "paragraph",
                    "attrs": {
                        "localId": "01323cc7-2373-4c77-9219-c1ac1baa7349"
                    },
                    "content": [
                        {
                            "text": "Next line.",
                            "type": "text"
                        }
                    ]
                },
                {
                    "type": "bulletList",
                    "attrs": {
                        "localId": "d9a2f49e-4bb9-485f-8e8d-d3eeff10dd53"
                    },
                    "content": [
                        {
                            "type": "listItem",
                            "attrs": {
                                "localId": "59a6d922-ba53-4dc0-ae69-01f28537cbd5"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "cb56e9b3-6682-4a67-b03e-bc06031b07b5"
                                    },
                                    "content": [
                                        {
                                            "text": "Bullet 1",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            "type": "listItem",
                            "attrs": {
                                "localId": "93c0102c-4cd4-43f8-b4e7-61981dfa514d"
                            },
                            "content": [
                                {
                                    "type": "paragraph",
                                    "attrs": {
                                        "localId": "fb240540-bce8-4b46-939c-8c2fbc8b6eb7"
                                    },
                                    "content": [
                                        {
                                            "text": "Bullet 2",
                                            "type": "text"
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "type": "paragraph",
            "attrs": {
                "localId": "563cac18-eec5-44a3-aade-532b8be32056"
            }
        }
    ],
    "version": 1
}
"""
}
