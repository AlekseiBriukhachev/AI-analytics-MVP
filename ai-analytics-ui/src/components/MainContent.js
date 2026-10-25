import DatasetManager from "./DatasetManager";
import ModelManager from "./ModelManager";

function MainContent({ onRefresh }) {
    return (
        <div className="main-content">
            <DatasetManager onRefresh={onRefresh} />
            <ModelManager onRefresh={onRefresh} />
        </div>
    );
}

export default MainContent;