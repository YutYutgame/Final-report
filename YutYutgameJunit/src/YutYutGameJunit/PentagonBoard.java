package YutYutGame;

import java.awt.Point;
import java.util.*;

public class PentagonBoard extends Board {
   
   private final Map<Integer, List<Integer>> adjacency = new HashMap<>();
    
  
    public PentagonBoard() {
        initNodes();
        initEdges();
    }
    @Override
    protected void initNodes() {
         nodes.put(0, new BoardNode(0, new Point(350, 475), false));
          nodes.put(1, new BoardNode(1, new Point(375, 435), false));
          nodes.put(2, new BoardNode(2, new Point(400, 395), false));
          nodes.put(3, new BoardNode(3, new Point(425, 355), false));
          nodes.put(4, new BoardNode(4, new Point(450, 315), false));
          nodes.put(5, new BoardNode(5, new Point(450, 275), false));
          nodes.put(6, new BoardNode(6, new Point(415, 230), false));
          nodes.put(7, new BoardNode(7, new Point(375, 190), false));
          nodes.put(8, new BoardNode(8, new Point(335, 150), false));
          nodes.put(9, new BoardNode(9, new Point(295, 110), false));
          nodes.put(10, new BoardNode(10, new Point(255, 60), false));
          nodes.put(11, new BoardNode(11, new Point(215, 110), false));
          nodes.put(12, new BoardNode(12, new Point(175, 150), false));
          nodes.put(13, new BoardNode(13, new Point(135, 190), false));
          nodes.put(14, new BoardNode(14, new Point(95, 230), false));
          nodes.put(15, new BoardNode(15, new Point(55, 275), false));
          nodes.put(16, new BoardNode(16, new Point(55, 315), false));
          nodes.put(17, new BoardNode(17, new Point(80, 355), false));
          nodes.put(18, new BoardNode(18, new Point(105, 395), false));
          nodes.put(19, new BoardNode(19, new Point(130, 435), false));
          nodes.put(20, new BoardNode(20, new Point(155, 475), false));
          nodes.put(21, new BoardNode(21, new Point(200, 475), false));
          nodes.put(22, new BoardNode(22, new Point(240, 475), false));
          nodes.put(23, new BoardNode(23, new Point(280, 475), false));
          nodes.put(24, new BoardNode(24, new Point(320, 475), false));
          nodes.put(25, new BoardNode(25, new Point(250, 275), true));
          nodes.put(26, new BoardNode(26, new Point(250, 185), false));
          nodes.put(27, new BoardNode(27, new Point(250, 145), false));
          nodes.put(28, new BoardNode(28, new Point(250, 225), false));
          nodes.put(29, new BoardNode(29, new Point(290, 275), false));
          nodes.put(30, new BoardNode(30, new Point(330, 275), false));
          nodes.put(31, new BoardNode(31, new Point(370, 275), false));
          nodes.put(32, new BoardNode(32, new Point(275, 340), false));
          nodes.put(33, new BoardNode(33, new Point(300, 370), false));
          nodes.put(34, new BoardNode(34, new Point(325, 410), false));
          nodes.put(35, new BoardNode(35, new Point(210, 275), false));
          nodes.put(36, new BoardNode(36, new Point(170, 275), false));
          nodes.put(37, new BoardNode(37, new Point(130, 275), false));
          nodes.put(38, new BoardNode(38, new Point(225, 340), false));
          nodes.put(39, new BoardNode(39, new Point(200,370), false));
          nodes.put(40, new BoardNode(40, new Point(175,410), false));
         
    }


       

   
    @Override
    protected void initEdges() {
        link(5, 31); link(31, 30); link(30, 29); link(29, 25);
        link(10, 27); link(27, 26); link(26, 28); link(28, 25);
        link(15, 37); link(37, 36); link(36, 35); link(35, 25);
        
        link(25, 38); link(38, 39); link(39, 40); link(40, 20);
        link(25, 32); link(32, 33); link(33, 34); link(34, 0);
        }

    private void link(int from, int to) {
        adjacency.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }
    
    @Override
    public BoardNode getNode(int id) {
        return nodes.get(id);
    }

    @Override
    public List<Integer> getNextNodes(int currentId, int prevId, boolean isCenter, int i) {
        
       if(isCenter && i == 0) {
          return Collections.singletonList(25);
       }
       else {
          if (isForcedDiagonal(currentId) && i == 0) {
             System.out.println("▶ currentId = " + currentId + ", isForcedDiagonal = " + isForcedDiagonal(currentId));

                return getForcedNext(currentId).map(Collections::singletonList).orElse(Collections.emptyList());
            }else if(isOuterNode(currentId)){
               return Collections.singletonList((currentId + 1) % 25);
            }else {
                return adjacency.getOrDefault(currentId, Collections.emptyList());
            }
        }
       
    }



    @Override
    public List<Integer> getPrevNodes(int nodeId) {
        if (isOuterNode(nodeId)) {
            int prevId = (nodeId - 1 + 25) % 25;
            return Collections.singletonList(prevId);
        }
        
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : adjacency.entrySet()) {
            if (entry.getValue().contains(nodeId)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private boolean isOuterNode(int id) {
        return id >= 0 && id <= 24;
    }

    public boolean isForcedDiagonal(int id) {
        return id == 5 || id == 10 || id == 15;
    }

    public Optional<Integer> getForcedNext(int id) {
        if (id == 5) return Optional.of(31);
        else if (id == 10) return Optional.of(27);
        else if (id == 15) return Optional.of(37);
 
        return Optional.empty();
    }
    public boolean shouldEscape(Piece piece, int currentNodeId, boolean hasMoved, int stepIndex) {
        if (!hasMoved) return false;

        switch (currentNodeId) {
            case 0: return stepIndex >= 1;
            default: return false;
        }
    }
    
    //junit
    @Override
    public BoardNode getStartNode() {
        return getNode(0); // 시작 노드 ID가 0이면
    }
}

