/**
 * Static product catalog for the AgriTech Marketplace demo.
 * category values must match the data-filter chips in index.html:
 * tomato | potato | grapes | wheat | leafy | orchard
 */
const PRODUCTS = [
  {
    id: "p1",
    name: "Bio-NPK Compost Mix",
    category: "tomato",
    price: 349,
    unit: "10 kg bag",
    organic: true,
    tags: ["Compost", "Flowering stage"],
    image: "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=600&q=80",
    description:
      "A balanced nitrogen-phosphorus-potassium compost blend formulated for tomato plants entering the flowering and fruit-set stage. Improves soil structure and supports steady, healthy yields."
  },
  {
    id: "p2",
    name: "Neem Oil Concentrate",
    category: "tomato",
    price: 219,
    unit: "500 ml bottle",
    organic: true,
    tags: ["Pest control", "Organic"],
    image: "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?auto=format&fit=crop&w=600&q=80",
    description:
      "Cold-pressed neem oil concentrate for natural pest and mildew control on tomato and leafy crops. Dilute with water and apply during early morning or evening hours."
  },
  {
    id: "p3",
    name: "Potato Tuber Booster",
    category: "potato",
    price: 289,
    unit: "5 kg pack",
    organic: false,
    tags: ["Tuber growth", "Potash rich"],
    image: "https://images.unsplash.com/photo-1518977676601-b53f82aba655?auto=format&fit=crop&w=600&q=80",
    description:
      "Potash-rich granular feed designed to support tuber bulking in potato crops. Apply during the vegetative-to-tuberization transition for best results."
  },
  {
    id: "p4",
    name: "Vermicompost — Premium Grade",
    category: "potato",
    price: 179,
    unit: "10 kg bag",
    organic: true,
    tags: ["Soil health", "Slow release"],
    image: "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=600&q=80",
    description:
      "Fully matured earthworm castings that improve water retention and micro-nutrient availability in the root zone. Suitable for potato, root vegetables, and general field use."
  },
  {
    id: "p5",
    name: "Grapevine Shield Spray",
    category: "grapes",
    price: 459,
    unit: "1 litre bottle",
    organic: false,
    tags: ["Fungal protection", "Vineyard"],
    image: "https://images.unsplash.com/photo-1596363505729-4190a9506133?auto=format&fit=crop&w=600&q=80",
    description:
      "Preventive fungicidal spray formulated for grapevines, targeting common vineyard fungal pressure during humid growing seasons. Rotate with organic alternatives for resistance management."
  },
  {
    id: "p6",
    name: "Grape Cluster Nutrient Kit",
    category: "grapes",
    price: 599,
    unit: "kit (3 items)",
    organic: true,
    tags: ["Berry set", "Micronutrients"],
    image: "https://images.unsplash.com/photo-1599819177626-b6e6e5b6c6a0?auto=format&fit=crop&w=600&q=80",
    description:
      "A three-part micronutrient kit for grape clusters covering pre-bloom, berry-set, and veraison stages. Supports even ripening and cluster quality."
  },
  {
    id: "p7",
    name: "Wheat Growth Accelerator",
    category: "wheat",
    price: 329,
    unit: "5 kg pack",
    organic: false,
    tags: ["Tillering stage", "Nitrogen boost"],
    image: "https://images.unsplash.com/photo-1500595046743-cd271d694d30?auto=format&fit=crop&w=600&q=80",
    description:
      "Fast-acting nitrogen formula to support wheat during the tillering stage, encouraging stronger stands and improved grain fill later in the season."
  },
  {
    id: "p8",
    name: "Wheat Seed Treatment Powder",
    category: "wheat",
    price: 149,
    unit: "1 kg pack",
    organic: true,
    tags: ["Seed protection", "Pre-sowing"],
    image: "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?auto=format&fit=crop&w=600&q=80",
    description:
      "Bio-based seed treatment powder that protects wheat seed against soil-borne pathogens during germination. Apply as a light coating before sowing."
  },
  {
    id: "p9",
    name: "Leafy Greens Foliar Feed",
    category: "leafy",
    price: 199,
    unit: "500 ml bottle",
    organic: true,
    tags: ["Foliar spray", "Fast greening"],
    image: "https://images.unsplash.com/photo-1576045057995-568f588f82fb?auto=format&fit=crop&w=600&q=80",
    description:
      "Nitrogen-forward foliar spray for spinach, lettuce, and other leafy greens, promoting fast, even leaf development with minimal runoff."
  },
  {
    id: "p10",
    name: "Leafy Crop Pest Net Kit",
    category: "leafy",
    price: 899,
    unit: "kit (10 sq. m)",
    organic: true,
    tags: ["Physical barrier", "Chemical-free"],
    image: "https://images.unsplash.com/photo-1592841200221-a6898f307baa?auto=format&fit=crop&w=600&q=80",
    description:
      "Fine-mesh insect netting kit covering 10 square metres of bed space, offering chemical-free pest exclusion for leafy green crops."
  },
  {
    id: "p11",
    name: "Orchard Root Feed Stakes",
    category: "orchard",
    price: 649,
    unit: "pack of 20",
    organic: false,
    tags: ["Slow release", "Deep root"],
    image: "https://images.unsplash.com/photo-1560493676-04071c5f467b?auto=format&fit=crop&w=600&q=80",
    description:
      "Slow-release fertilizer stakes for driving nutrients directly to the root zone of fruit trees. Ideal for orchard maintenance across the growing season."
  },
  {
    id: "p12",
    name: "Orchard Copper Fungicide",
    category: "orchard",
    price: 379,
    unit: "1 kg pack",
    organic: true,
    tags: ["Disease control", "Dormant spray"],
    image: "https://images.unsplash.com/photo-1560493676-4a3958b3a4f0?auto=format&fit=crop&w=600&q=80",
    description:
      "Copper-based dormant-season spray for orchard trees, reducing overwintering fungal and bacterial disease pressure ahead of bud break."
  }
];

const CATEGORY_LABELS = {
  tomato: "Tomato",
  potato: "Potato",
  grapes: "Grapes",
  wheat: "Wheat",
  leafy: "Leafy greens",
  orchard: "Orchards"
};
