using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using GMap.NET.WindowsForms.Markers;
using GMap.NET.WindowsForms;
using GMap.NET.MapProviders;
using Parse;
namespace WindowsFormsApplication2
{
    public partial class Form1 : Form
    {
        
        IEnumerable<ParseObject> lati, lng;//store the coordinate from parse
        GMapOverlay markerOverlay = new GMapOverlay();
        public Form1()
        {
           
            InitializeComponent();
        }
        private void Form1_Load(object sender, EventArgs e)
        {
            // Initialize parse          
            ParseClient.Initialize("xAT9ru3zhAMjXlP4tiCS0ENf4BYLgnZr4sgdh8ua", "YkyU6SxRhrgN07K5Dov56TXqLIHAaO67b8fSecrb");

            GetMessages();
            // Initialize Acgis map
            gMapControl1.MapProvider = GMap.NET.MapProviders.ArcGIS_StreetMap_World_2D_MapProvider.Instance;
            //gMapControl1.MapProvider = GMap.NET.MapProviders.

            GMap.NET.GMaps.Instance.Mode = GMap.NET.AccessMode.ServerOnly; //mode

            //resize the layer 
            gMapControl1.SetBounds(0, 0, ClientRectangle.Width, ClientRectangle.Height);
            pictureBox1.SetBounds(0, 0, ClientRectangle.Width, ClientRectangle.Height);
            //zoomIn and out
            gMapControl1.MinZoom = 1;
            gMapControl1.MaxZoom = 15;

            gMapControl1.GrayScaleMode = true;

            //picture behavoir
            pictureBox1.Parent = gMapControl1;
            pictureBox2.Parent = gMapControl1;


            //location
            gMapControl1.Position = new GMap.NET.PointLatLng(43.389758, -80.405068);
            gMapControl1.Zoom = 7;

            //43.389758
             //-80.405068
        }

        private void gMapControl1_Load(object sender, EventArgs e)
        {
          

        }

        private void button2_Click(object sender, EventArgs e)
        {

            //Drop a Waypoint Marker at a random location on campus

            int i = 0;
        }

        private void pictureBox2_Click(object sender, EventArgs e)
        {
            //Drop a Waypoint Marker at a random location on campus

            //Generate a very smalll double to add randomness to our waypoints
            //var r = new Random();

            //double randomLat = 43.389758 + 0.0001 * r.Next(100);
            //double randomLon = -80.405068 + 0.0001 * r.Next(100);            
            List<ParseObject> coor = coordinate.ToList();

            
            //shoot the waypoint on the screen
            for (int i = 0; i < coordinate.Count(); i++)
            {
                Double  randomLat = coor[i].Get<Double>("latitud");
                Double randomLon = coor[i].Get<Double>("longtitude");

                //Create waypoint location and style 
                GMarkerGoogle marker = new GMarkerGoogle(new GMap.NET.PointLatLng
                (randomLat, randomLon), GMarkerGoogleType.yellow_dot);

                //add waypoint to the transparent overlay
                markerOverlay.Markers.Add(marker);
                //add the overlay to the 
                gMapControl1.Overlays.Add(markerOverlay);
            }                    
        }

        IEnumerable<ParseObject> coordinate;
        private async void  GetMessages()
        {
            
            //var query = from gameScore in ParseObject.GetQuery("GpsCoordinate")
            //            where gameScore.Get<string>("latitud") != ""
            //            select gameScore;
            //IEnumerable<ParseObject> results = await query.FindAsync();

            var query = ParseObject.GetQuery("GpsCoordinate");
            coordinate = await query.FindAsync();         
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {

        }
    }
}
