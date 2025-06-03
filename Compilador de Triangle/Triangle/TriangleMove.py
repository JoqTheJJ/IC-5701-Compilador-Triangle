import os
import shutil

def moveTriangle(origen_folder, destino_folder, nombre_archivo):

    o = os.path.join(origen_folder, nombre_archivo)
    d = os.path.join(destino_folder, nombre_archivo)

    if not os.path.exists(o):
        print(f"El archivo {o} no existe.")
        return

    if os.path.exists(d):
        os.remove(d)
        print(f"Archivo existente {d} eliminado.")

    shutil.copy2(o, d)
    print(f"Archivo: {nombre_archivo} \nOrigen: {origen_folder} \nDestino: {destino_folder}\n")
    print("\n-------------------\n---< [ EXITO ] >---\n-------------------\n\n")


directorio_origen     = "C:\\Users\\PC\\Documents\\GitHub\\IC-5701-Compilador-Triangle\\Compilador de Triangle\\Triangle\\dist"
directorio_destino    = "C:\\Users\\PC\\Documents\\GitHub\\IC-5701-Compilador-Triangle\\Compilador de Triangle\\ide-triangle-v1.1.src"

directorio_origen     = directorio_origen.replace("\\", "/")
directorio_destino    = directorio_destino.replace("\\", "/")
triangle              = "Triangle.jar"

moveTriangle(directorio_origen, directorio_destino, triangle)