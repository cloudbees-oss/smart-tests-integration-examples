def pytest_collection_modifyitems(session, config, items):
    for item in items:
        for marker in item.iter_markers():
            item.user_properties.append(("marker_name", marker.name))
            item.user_properties.append(("marker_args", str(marker.args)))
            item.user_properties.append(("marker_kwargs", str(marker.kwargs)))
