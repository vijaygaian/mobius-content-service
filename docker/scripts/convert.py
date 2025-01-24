import argparse
import ruamel.yaml
from ruamel.yaml.scalarstring import DoubleQuotedScalarString as dq

def update_configmap(input_file, existing_yaml, output_yaml, remove_unmatched=False):
    properties = {}
    with open(input_file, 'r') as file:
        for line in file:
            line = line.strip()
            if line and not line.startswith('#') and '=' in line:
                key, value = line.split('=', 1)
                key = key.strip()
                value = value.strip()
                if value == "":
                    properties[key] = ruamel.yaml.scalarstring.PlainScalarString(value)
                else:
                    properties[key] = dq(value)

    yaml = ruamel.yaml.YAML()
    yaml.preserve_quotes = True
    yaml.indent(mapping=2, sequence=4, offset=2)
    with open(existing_yaml, 'r') as file:
        data = yaml.load(file)

    if 'configmap' not in data:
        data['configmap'] = {}

    updated = False
    remove_keys = set(data['configmap'].keys()) - set(properties.keys())
    for key, value in properties.items():
        if key not in data['configmap'] or data['configmap'][key] != value:
            data['configmap'][key] = value
            updated = True

    if remove_unmatched:
        for key in remove_keys:
            del data['configmap'][key]
            updated = True

    if updated:
        with open(output_yaml, 'w') as file:
            yaml.dump(data, file)

def main():
    parser = argparse.ArgumentParser(description='Update values.yaml based on properties file')
    parser.add_argument('input_file', help='Path to the properties file to read')
    args = parser.parse_args()

    existing_yaml = 'values.yaml'
    output_yaml = 'values.yaml'  # Writing back to the same file
    remove_unmatched = True  # This can be changed as needed

    update_configmap(args.input_file, existing_yaml, output_yaml, remove_unmatched)

if __name__ == "__main__":
    main()