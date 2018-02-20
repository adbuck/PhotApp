package photApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.Service;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.ows.WMSRequest;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetLegendGraphicRequest;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.map.MapContent;
import org.geotools.map.WMSLayer;
import org.geotools.ows.ServiceException;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.wms.WMSLayerChooser;

/**
 * This lab explores the use of the GeoTools WMS client code.
 * <p>
 * The GeoTools WMS client is a little bit more than simply sending away GetMap
 * request, the trick is to send the correct GetMap request based on the version
 * of the web map server specification the server supports, and be able to chose
 * layers and styles supported by the server.
 *
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class WMSloader extends JFrame {


   public WMSloader(JMapFrame frame) throws IOException, ServiceException {
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
       MapContent mapcontent = new MapContent();
       mapcontent.setTitle( wms.getCapabilities().getService().getTitle() );

       for( Layer wmsLayer : wmsLayers ){
           WMSLayer displayLayer = new WMSLayer(wms, wmsLayer );
           mapcontent.addLayer(displayLayer);
       }
       // Now display the map
       frame.showMap(mapcontent);
   }
}

