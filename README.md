## Sparse Order Picking - Navigation  | Android
This project contains all things Android regarding the Sparse Order Picking- Navigation project. <br>
It includes 4 modules: <b>Glass, Mobile, LocationTracker, SparseNavLib</b>. 

<p><b>Glass</b>: This module is an Androip App that runs on the Google Glass. It is mostly responsible for the UI/UX the 
Order-Picker sees. It interacts only with the Mobile App that is paired with it.</p>

<p><b>Mobile</b>: This module is an Androip App that runs on the Android device that is paired with the Glass.
It's the middle-man between the Server(WMS) and the HWD, providing the Glass with all the information it needs.</p>

<p><b>LocationTracker</b>: This module is an Androip App. The user of this application sees a top down map of the Warehouse
and with his finger can point to the location of the Order Picker. The location is send to the Server.</p>

<p><b>SparseNavLib</b>: This module is an Androip Library. All three Android Apps/Modules import this Library 
and use its functions and classes. Any code that applies to more than one App should be written here.</p>

