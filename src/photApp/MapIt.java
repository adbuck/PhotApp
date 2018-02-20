package photApp;

import com.vividsolutions.jts.geom.Geometry;
import javafx.application.Platform;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.ows.Layer;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wms.WebMapServer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.map.*;

import org.geotools.ows.ServiceException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.*;
import org.geotools.swing.JMapFrame;

import org.geotools.swing.wms.WMSLayerChooser;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.geotools.xml.PullParser;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;

import org.opengis.style.ContrastMethod;
import org.opengis.util.ProgressListener;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MapIt {


    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private GridCoverage2DReader reader;
    MapContent map = new MapContent();
    private JMapFrame frame = new JMapFrame(map);


    public void buildMap() throws Exception {


        map.setTitle("PhotMap");

        frame.setSize(800,600);
        frame.enableStatusBar(true);
        frame.enableToolBar(true);
        frame.enableLayerTable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Add Data");
        JMenu menuCompilers = new JMenu("Compilers");
        menuBar.add(menu);
        menuBar.add(menuCompilers);

/********************************************************************
ADD menu Items of all compilers
**********************************************************************/
        JMenuItem menuItemAdam = new JMenuItem("Adam");
        menuItemAdam.setToolTipText("See Adam's Progress");
        menuCompilers.add(menuItemAdam);
        menuItemAdam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Adam");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemDoug = new JMenuItem("Doug");
        menuItemDoug.setToolTipText("See Doug's Progress");
        menuCompilers.add(menuItemDoug);
        menuItemDoug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Doug");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemEric = new JMenuItem("Eric");
        menuItemEric.setToolTipText("See Eric's Progress");
        menuCompilers.add(menuItemEric);
        menuItemEric.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Eric");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemJohn = new JMenuItem("John");
        menuItemJohn.setToolTipText("See John's Progress");
        menuCompilers.add(menuItemJohn);
        menuItemJohn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"John");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemKaren = new JMenuItem("Karen");
        menuItemKaren.setToolTipText("See Karen's Progress");
        menuCompilers.add(menuItemKaren);
        menuItemKaren.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Karen");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemKristen = new JMenuItem("Kristen");
        menuItemKristen.setToolTipText("See Kristen's Progress");
        menuCompilers.add(menuItemKristen);
        menuItemKristen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Kristen");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemNancy = new JMenuItem("Nancy");
        menuItemNancy.setToolTipText("See Nancy's Progress");
        menuCompilers.add(menuItemNancy);
        menuItemNancy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Nancy");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemSteveM = new JMenuItem("Steve M");
        menuItemSteveM.setToolTipText("See Steve's Progress");
        menuCompilers.add(menuItemSteveM);
        menuItemSteveM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Steve M");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemSteveR = new JMenuItem("Steve R");
        menuItemSteveR.setToolTipText("See Steve's Progress");
        menuCompilers.add(menuItemSteveR);
        menuItemSteveR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Steve R");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuItemToby = new JMenuItem("Toby");
        menuItemToby.setToolTipText("See Toby's Progress");
        menuCompilers.add(menuItemToby);
        menuItemToby.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            PhotMapContent shpFile = new PhotMapContent();
                            QueryMapDB q = new QueryMapDB();
                            shpFile = q.queryMapDB(shpFile,"Toby");
                            displayCompilerShpFile(shpFile.getMapLink().getValue());

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        /**********************************************************************
         Add Data Menu Items - Call methods to load data
         **********************************************************************/

        JMenuItem menuItemWMS = new JMenuItem("Add WMS");
        menuItemWMS.setToolTipText("Add a WMS Layer to the map");
        menu.add(menuItemWMS);
        menuItemWMS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        try {
                            loadWMS();
                        } catch (Exception e1) {
                           e1.printStackTrace();
                        }
                  }
                });

        JMenuItem menuShapeItem = new JMenuItem("Add Shapefile");
        menuShapeItem.setToolTipText("Add a Shapefile to the map");
        menu.add(menuShapeItem);
        menuShapeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            displayShapefile();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuKMLItem = new JMenuItem("Add KML");
        menuKMLItem.setToolTipText("Add a KML to the map");
        menu.add(menuKMLItem);
        menuKMLItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadKML();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        JMenuItem menuRasterItem = new JMenuItem("Add Raster");
        menuRasterItem.setToolTipText("Add a Raster Image to the map");
        menu.add(menuRasterItem);
        menuRasterItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            displayRaster();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        /********************************************************************
         ADD Browse Button
         gets project link and calls method to update PhotMap DB
         **********************************************************************/

        JButton browseBtn = new JButton("...");
        frame.getJMenuBar().add(browseBtn);
        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = Controller.handleBrowseAction();
                            //Update PhotMap Data base
                            QueryMapDB update = new QueryMapDB();
                            String username = System.getProperty("user.name");
                            System.out.println("username = " + username);
                            update.updateProject(path, username);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        frame.setVisible(true);

    }
    /******************************************************************************************************************
     * Methods to ADD different types of data to map
     */
    /******************************************************************************************************************
     * Prompts the user for a shapefile (unless a filename is provided
     * on the command line; then creates a simple Style and displays
     * the shapefile on screen
     */
    public void displayRaster() throws Exception {
        String path = Controller.handleBrowseAction();
        File rasterFile= new File(path);
        AbstractGridFormat format = GridFormatFinder.findFormat( rasterFile );
        //this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }

        reader = format.getReader(rasterFile, hints);

        // Initially display the raster in greyscale using the
        // data from the first image band
        Style rasterStyle = createRGBStyle();
        GridReaderLayer rasterLayer = new GridReaderLayer(reader, rasterStyle);
        map.addLayer(rasterLayer);
        frame.showMap(map);
        frame.repaint();
    }
    /**
     * Prompts the user for a shapefile (unless a filename is provided
     * on the command line; then creates a simple Style and displays
     * the shapefile on screen
     */
    public void displayShapefile() throws Exception {

        String path = Controller.handleBrowseAction();
        File file = new File(path);

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource shapefileSource = store.getFeatureSource();

        // Create a basic Style to render the features
        Style shpStyle = SLD.createSimpleStyle(shapefileSource.getSchema());
        // Add the features and the associated Style object to
        // the MapContent as a new Layer
        FeatureLayer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
        map.addLayer(shpLayer);
        // Now display the map

        frame.showMap(map);
    }
    public void displayCompilerShpFile(String path) throws Exception {


        File file = new File(path);

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource shapefileSource = store.getFeatureSource();

        // Create a basic Style to render the features
        Style shpStyle = SLD.createSimpleStyle(shapefileSource.getSchema());
        // Add the features and the associated Style object to
        // the MapContent as a new Layer
        FeatureLayer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
        map.addLayer(shpLayer);
        // Now display the map

        frame.showMap(map);
    }
    /**
     * Load Web Map Server GIS Clearinghouse NYS Orthos
     */
    public void loadWMS() throws IOException, ServiceException {
        URL capabilitiesURL = new URL (" http://www.orthos.dhses.ny.gov/ArcGIS/services/Latest/MapServer/WMSServer?");
       if( capabilitiesURL == null ){
           System.exit(0); // canceled
       }
       WebMapServer wms = new WebMapServer( capabilitiesURL );

       List<Layer> wmsLayers = WMSLayerChooser.showSelectLayer( wms );
       if( wmsLayers == null ){
           JOptionPane.showMessageDialog(null, "Could not connect - check url");
           System.exit(0);
       }
       //MapContent mapcontent = new MapContent();
       map.setTitle( wms.getCapabilities().getService().getTitle() );

       for( Layer wmsLayer : wmsLayers ){
           WMSLayer displayLayer = new WMSLayer(wms, wmsLayer );
           map.addLayer(displayLayer);
       }
       // Now display the map
       frame.showMap(map);


    }
    public void loadKML() throws Exception {

        String path = Controller.handleBrowseAction();
        File kmlfile = new File(path);

        //kmlFile2FeatureCollection(kmlfile);
        SimpleFeatureSource kmlSource = DataUtilities.source( kmlFile2FeatureCollection(kmlfile));


        // Create a basic Style to render the features
       // Style kmlStyle = SLD.createSimpleStyle(kmlSource.getSchema());
        // Add the features and the associated Style object to
        // the MapContent as a new Layer
        FeatureLayer kmlLayer = new FeatureLayer(kmlSource,null);
        map.addLayer(kmlLayer);

        frame.showMap(map);
    }

    /**
     * Transform a kml file in a {@link FeatureCollection}.
     *
     * @param kml the file to convert.
     * @return the generated feature collection.
     * @throws Exception
     */
    public static final String[] IGNORED_ATTR = {"LookAt", "Style", "Region"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    public static final String KML_FILE_EXTENSION = "kml"; //$NON-NLS-1$

    public static final String KMZ_FILE_EXTENSION = "kmz"; //$NON-NLS-1$



    public static FeatureCollection<SimpleFeatureType, SimpleFeature> kmlFile2FeatureCollection( File kml ) throws Exception {
        InputStream inputStream = null;
        if (kml.getName().toLowerCase().endsWith(KMZ_FILE_EXTENSION)) {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(kml));
            ZipEntry entry = zis.getNextEntry();
            while (entry != null && !entry.getName().endsWith(KML_FILE_EXTENSION)) {
                entry = zis.getNextEntry();
            }
            if (entry == null) {
                throw new Exception("Exeption"); //$NON-NLS-1$
            }
            inputStream = zis;
        } else {
            inputStream = new FileInputStream(kml);
        }

        PullParser parser = new PullParser(new KMLConfiguration(), inputStream, KML.Placemark);

        DefaultFeatureCollection newCollection = new DefaultFeatureCollection();

        int index = 0;
        SimpleFeature f;
        DefaultGeographicCRS crs = DefaultGeographicCRS.WGS84;
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName(kml.getName());
        b.setCRS(crs);
        b.add("name", String.class);
        b.add("the_geom", Geometry.class); //$NON-NLS-1$
        SimpleFeatureType type = b.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);

        while( (f = (SimpleFeature) parser.parse()) != null ) {
            Geometry geometry = (Geometry) f.getDefaultGeometry();
            Object nameAttribute = null;
            try {
                nameAttribute = f.getAttribute("name");
            } catch (Exception e){
                // ignore name attribute
            }
            builder.addAll(new Object[]{nameAttribute, geometry });
            SimpleFeature feature = builder.buildFeature(type.getTypeName() + "." + index++); //$NON-NLS-1$
            newCollection.add(feature);
        }

        return newCollection;
    }



    /**
     * Figure out if a valid SLD file is available.
     */
    public File toSLDFile(File file)  {
        String path = file.getAbsolutePath();
        String base = path.substring(0,path.length()-4);
        String newPath = base + ".sld";
        File sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        return null;
    }
    /**
     * This method examines the names of the sample dimensions in the provided coverage looking for
     * "red...", "green..." and "blue..." (case insensitive match). If these names are not found
     * it uses bands 1, 2, and 3 for the red, green and blue channels. It then sets up a raster
     * symbolizer and returns this wrapped in a Style.
     *
     * @return a new Style object containing a raster symbolizer set up for RGB image
     */
    private Style createRGBStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

//}
//    /**
//     * Create a Style object from a definition in a SLD document
//     */
//    private Style createFromSLD(File sld) {
//        try {
//            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
//            Style[] style = stylereader.readXML();
//            return style[0];
//
//        } catch (Exception e) {
//            JExceptionReporter.showDialog(e, "Problem creating style");
//        }
//        return null;
//    }
}
