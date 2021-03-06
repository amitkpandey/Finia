package protect.Finia.NetWorthDataStructure;

import java.util.ArrayList;
import java.util.List;

import protect.Finia.Database.LiabilitiesType;

/**
 * The data processor to retrieve any data or data collection from the LiabilitiesTypeTree Structure.
 * The LiabilitiesTypeTree is designed to be read-only as the business logic of Finance
 * defines a fixed number of categories for net worth items.
 * The type processor is mostly used by the adapter of the Expandable List to retrieve the sub group of net worth items
 * in order to format the data stored in AssetsTypeTree so that they are compatible with the Expandable List widgets.
 * The node container is used so that the information is stored and the current level can be tracked.
 *
 * @author Owner  Kevin Zhijun Wang
 * @see LiabilitiesType
 * created on 2020/05/09
 */
public class TypeTreeProcessor_Liabilities {

    private List<TypeTreeLeaf_Liabilities> typesTree;

    public TypeTreeProcessor_Liabilities(List<TypeTreeLeaf_Liabilities> typesTree) {
        this.typesTree = typesTree;
    }

    /**
     * Retrieve the sub group items of a parent node.
     *
     * @param parentNodeName the name of the parent node for the sub group to be searched
     * @param level current level of the node
     * @return the sub group of containers
     */
    public List<NodeContainer_Liabilities> getSubGroup(String parentNodeName, int level) {
        List<NodeContainer_Liabilities> containersSubGroup = new ArrayList<>();

        if (level == 0) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName, liabilitiesTypeTreeLeaf.liabilitiesFirstLevelId, 0);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if (level == 1) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesFirstLevelName.equals(parentNodeName)
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName, liabilitiesTypeTreeLeaf.liabilitiesSecondLevelId, 1);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        } else if (level == 2) {
            for (TypeTreeLeaf_Liabilities liabilitiesTypeTreeLeaf : typesTree) {
                if (liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName != null
                        && liabilitiesTypeTreeLeaf.liabilitiesSecondLevelName.equals(parentNodeName)
                        && liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName != null) {

                    NodeContainer_Liabilities nodeContainer = new NodeContainer_Liabilities(liabilitiesTypeTreeLeaf.liabilitiesThirdLevelName, liabilitiesTypeTreeLeaf.liabilitiesThirdLevelId, 2);
                    addTypeToSubGroup(nodeContainer, containersSubGroup);
                }
            }
        }

        return containersSubGroup;
    }

    /**
     * Add the node container to the sub group.
     * The method first check if the current container already exists and the id is valid.
     * Then, the node container is added to the sub group
     *
     * @param nodeContainer the node container to be added.
     * @param containersSubGroup the container sub group of a particular node.
     */
    private void addTypeToSubGroup(NodeContainer_Liabilities nodeContainer, List<NodeContainer_Liabilities> containersSubGroup) {
        for (NodeContainer_Liabilities dataCarrier: containersSubGroup) {
            if (dataCarrier.liabilitiesId == nodeContainer.liabilitiesId && dataCarrier.liabilitiesId != 0) {
                return;
            }
        }
        containersSubGroup.add(nodeContainer);
    }
}
