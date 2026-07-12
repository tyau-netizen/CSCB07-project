package com.example.b07demosummer2024.model;


import com.example.b07demosummer2024.R;

public class DummyData {

    public static ArtifactItem createArtifact1() {
        return new ArtifactItem(
                "LOT-001",
                "Bronze Ritual Vessel",
                "A ceremonial bronze vessel used in ancestral rites.",
                Category.BRONZE_WARE,
                Material.BRONZE,
                DynastyPeriod.WESTERN_ZHOU
        ) {{
            setCulturalOrigin("Ancient China");
            setDimensions("30 x 25 x 20 cm");
            setConditionReport("Minor patina, intact");
            setCurrentLocation("Hall 4, East Wing");
            setAcquisitionMethod("Donation");
            setProvenance("Excavated at Anyang, 1972");
            setAccessionNumber("ACC-2023-001");
            setNotes("Exhibited in 'Ritual and Power'");
            setImageUri("android.resource://drawable/bronze_ritual_vessel.jpg");
            setImageInt(R.drawable.bronze_ritual_vessel);
            setSaved(false);
        }};
    }

    public static ArtifactItem createArtifact2() {
        return new ArtifactItem(
                "LOT-002",
                "Ming Dynasty Porcelain Vase",
                "Blue-and-white porcelain vase with dragon motif.",
                Category.CERAMICS,
                Material.LACQUERWARE,
                DynastyPeriod.QIN
        ) {{
            setCulturalOrigin("Jingdezhen, China");
            setDimensions("45 cm height, 28 cm diameter");
            setConditionReport("Crack on base, restored");
            setCurrentLocation("Gallery 12, Special Exhibition");
            setAcquisitionMethod("Purchase");
            setProvenance("From the collection of Lord Percival");
            setAccessionNumber("ACC-2023-045");
            setNotes("Featured in 'Ming Masterpieces' catalog");
            setImageUri("android.resource://drawable/artifact_porcelain_vase");
            setImageInt(R.drawable.ming_dynasty_porcelain_vase);
            setSaved(true);
        }};
    }

    public static ArtifactItem createArtifact3() {
        return new ArtifactItem(
                "LOT-003",
                "Egyptian Scarab Amulet",
                "Glazed steatite scarab with hieroglyphic inscription.",
                Category.GLASSWARE,
                Material.JADE,
                DynastyPeriod.THREE_KINGDOMS
        ) {{
            setCulturalOrigin("Egypt, Valley of the Kings");
            setDimensions("2.5 x 1.8 x 1 cm");
            setConditionReport("Glaze worn, but inscription legible");
            setCurrentLocation("Storage Room B, Egyptian Collection");
            setAcquisitionMethod("Bequest");
            setProvenance("From the tomb of Tutankhamun (unofficial)");
            setAccessionNumber("ACC-2022-118");
            setNotes("Currently being studied by Egyptology department");
            setImageUri("android.resource://drawable/artifact_scarab");
            setImageInt(R.drawable.egyptian_scarab);
            setSaved(false);
            System.out.println("I was in dummy data");
        }};
    }

    public static ArtifactItem createArtifact4() {
        return new ArtifactItem(
                "LOT-004",
                "Roman Marble Bust",
                "Portrait bust of a Roman senator, early imperial period.",
                Category.LACQUERWARE,
                Material.CERAMIC,
                DynastyPeriod.FIVE_DYNASTIES_TEN_KINGDOMS
        ) {{
            setCulturalOrigin("Rome, Italy");
            setDimensions("55 cm height (including base)");
            setConditionReport("Nose chipped, otherwise fine");
            setCurrentLocation("Hall 7, Roman Antiquities");
            setAcquisitionMethod("Loan from Vatican Museums");
            setProvenance("Found in the Roman Forum, 1890");
            setAccessionNumber("ACC-2024-007");
            setNotes("Temporary loan, returns in 2025");
            setImageUri("android.resource://drawable/artifact_roman_bust");
            setImageInt(R.drawable.roman_marble);
            setSaved(false);
        }};
    }

    public static ArtifactItem createArtifact5() {
        return new ArtifactItem(
                "LOT-005",
                "Qing Bronze Censer",
                "Ornate bronze incense burner with mythical beast handles.",
                Category.BRONZE_WARE,
                Material.BRONZE,
                DynastyPeriod.QING
        ) {{
            setCulturalOrigin("Beijing, China");
            setDimensions("25 cm height, 20 cm diameter");
            setConditionReport("Excellent condition, original patina preserved");
            setCurrentLocation("Hall 2, Religious Art Section");
            setAcquisitionMethod("Private donation");
            setProvenance("Formerly in the Forbidden City collection");
            setAccessionNumber("ACC-2024-005");
            setNotes("Used in imperial court ceremonies");
            setImageUri("android.resource://drawable/qing_censer.jpg");
            setImageInt(R.drawable.roman_marble);
            setSaved(false);
        }};
    }

    public static ArtifactItem createArtifact6() {
        return new ArtifactItem(
                "LOT-006",
                "Han Gold-Inlaid Bronze Mirror",
                "Intricate bronze mirror with gold and silver inlay depicting mythical creatures.",
                Category.BRONZE_WARE,
                Material.GOLD,
                DynastyPeriod.HAN
        ) {{
            setCulturalOrigin("Sichuan, China");
            setDimensions("18 cm diameter");
            setConditionReport("Inlay mostly intact, slight tarnish on edges");
            setCurrentLocation("Hall 5, Metalwork Gallery");
            setAcquisitionMethod("Excavation");
            setProvenance("Found in a Han tomb in Chengdu, 1995");
            setAccessionNumber("ACC-2024-018");
            setNotes("Featured in 'Mirrors of the Past' exhibition");
            setImageUri("android.resource://drawable/han_mirror.jpg");
            setImageInt(R.drawable.roman_marble);
            setSaved(false);
        }};
    }

    public static ArtifactItem createArtifact7() {
        return new ArtifactItem(
                "LOT-007",
                "Tang Stone Bodhisattva",
                "Seated stone bodhisattva with remnants of original polychrome pigment.",
                Category.ENAMEL_WARE,
                Material.STONE,
                DynastyPeriod.TANG
        ) {{
            setCulturalOrigin("Longmen Grottoes, China");
            setDimensions("65 cm height");
            setConditionReport("Head partially damaged, body well preserved");
            setCurrentLocation("Hall 8, Buddhist Art Wing");
            setAcquisitionMethod("Government transfer");
            setProvenance("Recovered from a temple site near Luoyang, 1978");
            setAccessionNumber("ACC-2024-033");
            setNotes("Currently undergoing conservation treatment");
            setImageUri("android.resource://drawable/tang_bodhisattva.jpg");
            setImageInt(R.drawable.roman_marble);
            setSaved(true);
        }};
    }

    public static ArtifactItem createArtifact8() {
        return new ArtifactItem(
                "LOT-008",
                "Ming Silk Landscape Scroll",
                "Handscroll painting of a misty river landscape with pavilions and scholars.",
                Category.DOCUMENTS_AND_ARCHIVES,
                Material.SILVER,
                DynastyPeriod.MING
        ) {{
            setCulturalOrigin("Suzhou, China");
            setDimensions("120 x 30 cm (unrolled)");
            setConditionReport("Some discoloration, minor tears professionally repaired");
            setCurrentLocation("Hall 10, Painting Gallery");
            setAcquisitionMethod("Donation from the Wu Family");
            setProvenance("Formerly in the imperial collection");
            setAccessionNumber("ACC-2024-047");
            setNotes("Displayed in rotating exhibitions due to light sensitivity");
            setImageUri("android.resource://drawable/ming_scroll.jpg");
            setImageInt(R.drawable.bronze_ritual_vessel);
            setSaved(false);
        }};
    }
}