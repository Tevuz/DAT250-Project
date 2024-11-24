import Fs from "fs"
import Path from "path"

const name = "vite-plugin-generate-views"

export default function generateViews() {
    return {
        name,
        async buildStart() {
            const sourcePath = Path.resolve("")
            const configPath = Path.resolve("src/config/clientConfig.json");
            const outputPath = Path.resolve("src/generated/Views.js");

            const config = JSON.parse(Fs.readFileSync(configPath, "utf-8"));

            const imports = [];
            const views = [];

            for (const [path, { name, component }] of Object.entries(config["client"]["paths"])) {
                const relative_path = Path.relative(Path.dirname(sourcePath), Path.relative(Path.dirname(outputPath), Path.resolve(component))).replace(/\\/g,'/');
                imports.push(`import ${name} from "${relative_path}";`);
                views.push(`"${path}": ${name}`)
            }

            const module =
                `// ---- Generated File (${name}) ----\n` +
                `${imports.join("\n")}\n\n` +
                `export const views = {\n` +
                `    ${views.join(",\n    ")}\n` +
                `};`


            Fs.writeFileSync(outputPath, module.trim());
        }
    }
}