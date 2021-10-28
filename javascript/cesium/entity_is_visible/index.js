const viewer = new Cesium.Viewer('cesiumContainer');
const camera = viewer.camera;

const position = Cesium.Cartesian3.fromDegrees(23.9036, 54.8985);


const someEntity = viewer.entities.add({
  name: 'Some Point',
  position: position,
  point: {
      pixelSize: 14,
      color: Cesium.Color.GREEN
  }
});

viewer.zoomTo(someEntity);


Sandcastle.addToolbarButton("Check if an entity is in the screen", function () {
  const frustum = camera.frustum;
  const cullingVolume = frustum.computeCullingVolume(
    camera.position,
    camera.direction,
    camera.up
  );

  // Bounding sphere of the entity.
  let boundingSphere = new Cesium.BoundingSphere();
  viewer.dataSourceDisplay.getBoundingSphere(someEntity, false, boundingSphere);
  
  
  // Check if the entity is visible in the screen.
  const intersection = cullingVolume.computeVisibility(boundingSphere);
  
  console.log(intersection);
  //  1: Cesium.Intersect.INSIDE
  //  0: Cesium.Intersect.INTERSECTING
  // -1: Cesium.Intersect.OUTSIDE
});
