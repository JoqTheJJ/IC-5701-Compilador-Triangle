/*
 * @(#)Interpreter.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 *
 */

package TAM;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
// Imports Agregados
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Interpreter {


  static String objectName;


// DATA STORE

  static int[] data = new int[2048];                    // MOD
  static List<Integer> pointers = new ArrayList<>();    // AGREGADO


// DATA STORE REGISTERS AND OTHER REGISTERS

  final static int
    CB = 0,
    SB = 0,
    HB = 1024,  // start of dynamic memory
    HL = 2048;  // upper bound of dynamic memory + 1 // Agregado

  static int
    CT, CP, ST, HT, LB, status, HP; // HP = Heap Pointer // Agregado

  // status values
  final static int
    running = 0, halted = 1, failedDataStoreFull = 2, failedInvalidCodeAddress = 3,
    failedInvalidInstruction = 4, failedOverflow = 5, failedZeroDivide = 6,
    failedIOError = 7;

  static long
    accumulator;

  static int content (int r) {
    // Returns the current content of register r,
    // even if r is one of the pseudo-registers L1..L6.

    switch (r) {
      case Machine.CBr:
        return CB;
      case Machine.CTr:
        return CT;
      case Machine.PBr:
        return Machine.PB;
      case Machine.PTr:
        return Machine.PT;
      case Machine.SBr:
        return SB;
      case Machine.STr:
        return ST;
      case Machine.HBr:
        return HB;
      case Machine.HTr:
        return HT;
      case Machine.LBr:
        return LB;
      
      //Añadido registros memoria dinamico
      case Machine.HLr:
        return HL;
      case Machine.HPr:
        return HP;
        
      case Machine.L1r:
        return data[LB];
      case Machine.L2r:
        return data[data[LB]];
      case Machine.L3r:
        return data[data[data[LB]]];
      case Machine.L4r:
        return data[data[data[data[LB]]]];
      case Machine.L5r:
        return data[data[data[data[data[LB]]]]];
      case Machine.L6r:
        return data[data[data[data[data[data[LB]]]]]];
      case Machine.CPr:
        return CP;
      default:
        return 0;
    }
  }


// PROGRAM STATUS

  static void dump() {
    // Writes a summary of the machine state.
    int
      addr, staticLink, dynamicLink,
      localRegNum;

    System.out.println ("");
    System.out.println ("State of data store and registers:");
    System.out.println ("");
    if (HT == HB)
      System.out.println("            |--------|          (heap is empty)");
    else {
      System.out.println("       HB-->");
      System.out.println("            |--------|");
      for (addr = HB - 1; addr >= HT; addr--) {
        System.out.print(addr + ":");
        if (addr == HT)
          System.out.print(" HT-->");
        else
          System.out.print("      ");
        System.out.println("|" + data[addr] + "|");
      }
      System.out.println("            |--------|");
    }
    System.out.println("            |////////|");
    System.out.println("            |////////|");
    if (ST == SB)
      System.out.println("            |--------|          (stack is empty)");
    else {
      dynamicLink = LB;
      staticLink = LB;
      localRegNum = Machine.LBr;
      System.out.println("      ST--> |////////|");
      System.out.println("            |--------|");
      for (addr = ST - 1; addr >= SB; addr--) {
        System.out.print(addr + ":");
        if (addr == SB)
          System.out.print(" SB-->");
        else if (addr == staticLink) {
          switch (localRegNum) {
            case Machine.LBr:
              System.out.print(" LB-->");
              break;
            case Machine.L1r:
              System.out.print(" L1-->");
              break;
            case Machine.L2r:
              System.out.print(" L2-->");
              break;
            case Machine.L3r:
              System.out.print(" L3-->");
              break;
            case Machine.L4r:
              System.out.print(" L4-->");
              break;
            case Machine.L5r:
              System.out.print(" L5-->");
              break;
            case Machine.L6r:
              System.out.print(" L6-->");
              break;
          }
          staticLink = data[addr];
          localRegNum = localRegNum + 1;
        } else
          System.out.print("      ");
        if ((addr == dynamicLink) && (dynamicLink != SB))
          System.out.print("|SL=" + data[addr] + "|");
        else if ((addr == dynamicLink + 1) && (dynamicLink != SB))
          System.out.print("|DL=" + data[addr] + "|");
        else if ((addr == dynamicLink + 2) && (dynamicLink != SB))
          System.out.print("|RA=" + data[addr] + "|");
        else
          System.out.print("|" + data[addr] + "|");
        System.out.println ("");
        if (addr == dynamicLink) {
          System.out.println("            |--------|");
          dynamicLink = data[addr + 1];
        }
      }
    }
    System.out.println ("");
  }

  static void showStatus () {
    // Writes an indication of whether and why the program has terminated.
    System.out.println ("");
    switch (status) {
      case running:
        System.out.println("Program is running.");
        break;
      case halted:
        System.out.println("Program has halted normally.");
        break;
      case failedDataStoreFull:
        System.out.println("Program has failed due to exhaustion of Data Store.");
        break;
      case failedInvalidCodeAddress:
        System.out.println("Program has failed due to an invalid code address.");
        break;
      case failedInvalidInstruction:
        System.out.println("Program has failed due to an invalid instruction.");
        break;
      case failedOverflow:
        System.out.println("Program has failed due to overflow.");
        break;
      case failedZeroDivide:
        System.out.println("Program has failed due to division by zero.");
        break;
      case failedIOError:
        System.out.println("Program has failed due to an IO error.");
        break;
    }
    if (status != halted)
      dump();
  }


// INTERPRETATION

  static void checkSpace (int spaceNeeded) {
    // Signals failure if there is not enough space to expand the stack or
    // heap by spaceNeeded.

    if (HT - ST < spaceNeeded)
      status = failedDataStoreFull;
  }

  static boolean isTrue (int datum) {
    // Tests whether the given datum represents true.
    return (datum == Machine.trueRep);
  }

  static boolean equal (int size, int addr1, int addr2) {
    // Tests whether two multi-word objects are equal, given their common
    // size and their base addresses.

    boolean eq;
    int index;

    eq = true;
    index = 0;
    while (eq && (index < size))
      if (data[addr1 + index] == data[addr2 + index])
        index = index + 1;
      else
        eq = false;
    return eq;
  }

  static int overflowChecked (long datum) {
    // Signals failure if the datum is too large to fit into a single word,
    // otherwise returns the datum as a single word.

    if ((-Machine.maxintRep <= datum) && (datum <= Machine.maxintRep))
      return (int) datum;
    else {
      status = failedOverflow;
      return 0;
    }
  }

  static int toInt(boolean b) {
    return b ? Machine.trueRep : Machine.falseRep;
  }

  static int currentChar;

  static int readInt() throws java.io.IOException {
    int temp = 0;
    int sign = 1;

    do {
      currentChar = System.in.read();
    } while (Character.isWhitespace((char) currentChar));

    if ((currentChar == '-') || (currentChar == '+'))
      do {
        sign = (currentChar == '-') ? -1 : 1;
        currentChar = System.in.read();
      } while ((currentChar == '-')  || currentChar == '+');

    if (Character.isDigit((char) currentChar))
      do {
        temp = temp * 10 + (currentChar - '0');
        currentChar = System.in.read();
      } while (Character.isDigit((char) currentChar));

    return sign * temp;
  }

  static void callPrimitive (int primitiveDisplacement) {
    // Invokes the given primitive routine.

    int addr, size;
    char ch;

    switch (primitiveDisplacement) {
      case Machine.idDisplacement:
        break; // nothing to be done
      case Machine.notDisplacement:
        data[ST - 1] = toInt(!isTrue(data[ST - 1]));
        break;
      case Machine.andDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(isTrue(data[ST - 1]) & isTrue(data[ST]));
        break;
      case Machine.orDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(isTrue(data[ST - 1]) | isTrue(data[ST]));
        break;
      case Machine.succDisplacement:
        data[ST - 1] = overflowChecked(data[ST - 1] + 1);
        break;
      case Machine.predDisplacement:
        data[ST - 1] = overflowChecked(data[ST - 1] - 1);
        break;
      case Machine.negDisplacement:
        data[ST - 1] = -data[ST - 1];
        break;
      case Machine.addDisplacement:
        ST = ST - 1;
        accumulator = data[ST - 1];
        data[ST - 1] = overflowChecked(accumulator + data[ST]);
        break;
      case Machine.subDisplacement:
        ST = ST - 1;
        accumulator = data[ST - 1];
        data[ST - 1] = overflowChecked(accumulator - data[ST]);
        break;
      case Machine.multDisplacement:
        ST = ST - 1;
        accumulator = data[ST - 1];
        data[ST - 1] = overflowChecked(accumulator * data[ST]);
        break;
      case Machine.divDisplacement:
        ST = ST - 1;
        accumulator = data[ST - 1];
        if (data[ST] != 0)
          data[ST - 1] = (int) (accumulator / data[ST]);
        else
          status = failedZeroDivide;
        break;
      case Machine.modDisplacement:
        ST = ST - 1;
        accumulator = data[ST - 1];
        if (data[ST] != 0)
          data[ST - 1] = (int) (accumulator % data[ST]);
        else
          status = failedZeroDivide;
        break;
      case Machine.ltDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(data[ST - 1] < data[ST]);
        break;
      case Machine.leDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(data[ST - 1] <= data[ST]);
        break;
      case Machine.geDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(data[ST - 1] >= data[ST]);
        break;
      case Machine.gtDisplacement:
        ST = ST - 1;
        data[ST - 1] = toInt(data[ST - 1] > data[ST]);
        break;
      case Machine.eqDisplacement:
        size = data[ST - 1]; // size of each comparand
        ST = ST - 2 * size;
        data[ST - 1] = toInt(equal(size, ST - 1, ST - 1 + size));
        break;
      case Machine.neDisplacement:
        size = data[ST - 1]; // size of each comparand
        ST = ST - 2 * size;
        data[ST - 1] = toInt(! equal(size, ST - 1, ST - 1 + size));
        break;
      case Machine.eolDisplacement:
        data[ST] = toInt(currentChar == '\n');
        ST = ST + 1;
        break;
      case Machine.eofDisplacement:
        data[ST] = toInt(currentChar == -1);
        ST = ST + 1;
        break;
      case Machine.getDisplacement:
        ST = ST - 1;
        addr = data[ST];
        try {
          currentChar = System.in.read();
        } catch (java.io.IOException s) {
          status = failedIOError;
        }
        data[addr] = (int) currentChar;
        break;
      case Machine.putDisplacement:
        ST = ST - 1;
        ch = (char) data[ST];
        System.out.print(ch);
        break;
      case Machine.geteolDisplacement:
        try {
          while ((currentChar = System.in.read()) != '\n');
        } catch (java.io.IOException s) {
          status = failedIOError;
        }
        break;
      case Machine.puteolDisplacement:
        System.out.println ("");
        break;
      case Machine.getintDisplacement:
        ST = ST - 1;
        addr = data[ST];
        try {
          accumulator = readInt();
        } catch (java.io.IOException s) {
          status = failedIOError;
        }
        data[addr] = (int) accumulator;
        break;
      case Machine.putintDisplacement:
        ST = ST - 1;
        accumulator = data[ST];
        System.out.print(accumulator);
        break;
      case Machine.newDisplacement:
        size = data[ST - 1];
        checkSpace(size);
        HT = HT - size;
        data[ST - 1] = HT;
        break;
      case Machine.disposeDisplacement:
        ST = ST - 1; // no action taken at present
        break;
      case Machine.heapAllocAddr:
          size = data[ST - 1]; // Ocupo el espacio del objeto al inicio de la pila estatica
          System.out.println("Holi, el size en la pila es de: " + size);
          
          if (HP + size + 1 >= HL) { // Validar que hay espacio
              status = failedDataStoreFull;
          } else {
              data[HP] = size; // Guarda un bite con el tamaño del objeto
              data[ST - 1] = HP; // Deja en memeoria la direccion de memoria del objeto
              HP += size + 1; // Reserva el espacio de memoria para el objeto y su bite de size
          }
          break;
      case Machine.heapFreeAddr:
          addr = data[ST - 1]; // Ocupo la direccion que debo limpiar en el tope de la pila
          ST--; // Liberar memoria
          System.out.println("Holi, soy addr de delete y tengo: " + addr);
          
          if (HB > addr || addr >= HL) { // Validacion de si el objeto esta dentro del heap dinamico HB <= addr < HL
              status = failedInvalidInstruction;
              break;
          }
          
          size = data[addr] + 1; // Obtiene el espacio del objeto
          
          for (int i = addr; i < HP - size; i++) { // Baja los objetos segun el size
              data[i] = data[i + size];
          }
          
          for (int i = HP - size; i < HP; i++) { // Limpia los espacios desocupados
              data[i] = 0;
          }
          
          for (int i = 0; i < pointers.size(); i++) { // Actualizar los punteros que movimos
              int pointedAddr = data[pointers.get(i)];
              if (addr < pointedAddr) {
                  data[pointers.get(i)] -= size;
              }
          }
          
          HP -= size; // Liberar memoria
          break;
      case Machine.savePointerAddr:
          pointers.add(ST);
          break;
      case Machine.heapDeRefAddr:
          addr = data[ST - 1]; // Ocupo la direccion que debo recuperar
          ST--; // Liberar memoria
          
          if (HB > addr || addr >= HL) { // Validacion de si el objeto esta dentro del heap dinamico HB <= addr < HL
              status = failedInvalidInstruction;
              break;
          }
          
          size = data[addr]; //Obtengo el espacio del objeto en memoria
          
          if (ST + size >= HB) { // Validacion de si hay espacio suficiente en la pila estatica
              status = failedDataStoreFull;
              break;
          }
          
          for (int i = 0; i < size; i++) { //Copia el objeto de la memoria dinamica a la memoria estatica
              data[ST + i] = data[addr + i + 1];
          }
          
          ST = ST + size; // Aumentamos el puntero de la pila
          break;
      case Machine.heapStoreOp:
          addr = data[ST - 1]; // Ocupo la direccion que debo recuperar
          ST--;
          
          if (HB > addr || addr >= HL) { // Validacion de si el objeto esta dentro del heap dinamico HB <= addr < HL
              status = failedInvalidInstruction;
              break;
          }
          
          size = data[addr]; // Obtengo el espacio del objeto en memoria
          
          for (int i = 0; i < size; i++) { // Movemos el objeto a la memoria dinamica
              data[addr + i + 1] = data[ST - size + i];
          }
          
          ST = ST - size; // Eliminamos el objeto de la memoria estatica
          
          break;
      case Machine.heapPrint:
          System.out.println("Memoria Dinamica");
          System.out.println("----------------------------------------");
          for (int i = HB; i < HP; i++) {
              System.out.print("| [" + i + "] = " + data[i]);
              if (i == HB) {
                  System.out.println("   <-  [HB]");
              } else {
                  System.out.println("");
              }
          }
          System.out.println("----------------------------------------");
          System.out.println("[HP] : " + HP + "/2048");
          System.out.println("----------------------------------------");
          System.out.println("[HL] : " + HL);
          System.out.print("Punteros Activos: ");
          for (int i = 0; i < pointers.size(); i++) {
              int x = pointers.get(i);
              System.out.print("[" + x + " | " + data[x] + "]\t");
          }
          System.out.println("Adios :D\n");
          
          break;
    }
  }

  static void interpretProgram() {
    // Runs the program in code store.

    Instruction currentInstr;
    int op, r, n, d, addr, index;

    // Initialize registers ...
    ST = SB;    // 0
    HT = HB;    // 1024 Crece hacia abajo
    LB = SB;    // 0
    CP = CB;    // 0
    HP = HB;    // 1024 Crece hacia arriba // Agregado
    status = running;
    do {
      // Fetch instruction ...
      currentInstr = Machine.code[CP];
      // Decode instruction ...
      op = currentInstr.op;
      r = currentInstr.r;
      n = currentInstr.n;
      d = currentInstr.d;
      
      System.out.println("Holi, mi status: " + status);
      System.out.println("para mi proximo truco de magia: op=" + op + ", r=" + r + ", n=" + n + ", d=" + d);
      
      // Execute instruction ...
      switch (op) {
        case Machine.LOADop:
          addr = d + content(r);
          checkSpace(n);
          for (index = 0; index < n; index++)
            data[ST + index] = data[addr + index];
          ST = ST + n;
          CP = CP + 1;
          break;
        case Machine.LOADAop:
          addr = d + content(r);
          checkSpace(1);
          data[ST] = addr;
          ST = ST + 1;
          CP = CP + 1;
          break;
        case Machine.LOADIop:
          ST = ST - 1;
          addr = data[ST];
          checkSpace(n);
          for (index = 0; index < n; index++)
            data[ST + index] = data[addr + index];
          ST = ST + n;
          CP = CP + 1;
          break;
        case Machine.LOADLop:
          checkSpace(1);
          data[ST] = d;
          ST = ST + 1;
          CP = CP + 1;
          break;
        case Machine.STOREop:
          addr = d + content(r);
          ST = ST - n;
          for (index = 0; index < n; index++)
            data[addr + index] = data[ST + index];
          CP = CP + 1;
          break;
        case Machine.STOREIop:
          ST = ST - 1;
          addr = data[ST];
          ST = ST - n;
          for (index = 0; index < n; index++)
            data[addr + index] = data[ST + index];
          CP = CP + 1;
          break;
        case Machine.CALLop:
          addr = d + content(r);
          
          System.out.println("Holi, soy PB y contengo: " + content(r));
          System.out.println("Holi, soy addr: " + addr);
          
          if (addr >= Machine.PB) {
            callPrimitive(addr - Machine.PB);
            CP = CP + 1;
          } else {
            checkSpace(3);
            if ((0 <= n) && (n <= 15))
              data[ST] = content(n); // static link
            else
              status = failedInvalidInstruction;
            data[ST + 1] = LB; // dynamic link
            data[ST + 2] = CP + 1; // return address
            LB = ST;
            ST = ST + 3;
            CP = addr;
          }
          break;
        case Machine.CALLIop:
          ST = ST - 2;
          addr = data[ST + 1];
          if (addr >= Machine.PB) {
            callPrimitive(addr - Machine.PB);
            CP = CP + 1;
          } else {
            // data[ST] = static link already
            data[ST + 1] = LB; // dynamic link
            data[ST + 2] = CP + 1; // return address
            LB = ST;
            ST = ST + 3;
            CP = addr;
          }
          break;
        case Machine.RETURNop:
          addr = LB - d;
          CP = data[LB + 2];
          LB = data[LB + 1];
          ST = ST - n;
          for (index = 0; index < n; index++)
            data[addr + index] = data[ST + index];
          ST = addr + n;
          break;
        case Machine.PUSHop:
          checkSpace(d);
          ST = ST + d;
          CP = CP + 1;
          break;
        case Machine.POPop:
          addr = ST - n - d;
          ST = ST - n;
          for (index = 0; index < n; index++)
            data[addr + index] = data[ST + index];
          ST = addr + n;
          CP = CP + 1;
          break;
        case Machine.JUMPop:
          CP = d + content(r);
          break;
        case Machine.JUMPIop:
          ST = ST - 1;
          CP = data[ST];
          break;
        case Machine.JUMPIFop:
          ST = ST - 1;
          if (data[ST] == n)
            CP = d + content(r);
          else
            CP = CP + 1;
          break;
        case Machine.HALTop:
          status = halted;
          break;
      }
      if ((CP < CB) || (CP >= CT))
        status = failedInvalidCodeAddress;
    } while (status == running);
  }


// LOADING

  static void loadObjectProgram (String objectName) {
    // Loads the TAM object program into code store from the named file.

    FileInputStream objectFile = null;
    DataInputStream objectStream = null;

    int addr;
    boolean finished = false;

    try {
      objectFile = new FileInputStream (objectName);
      objectStream = new DataInputStream (objectFile);

      addr = Machine.CB;
      while (!finished) {
        Machine.code[addr] = Instruction.read(objectStream);
        if (Machine.code[addr] == null)
          finished = true;
        else
          addr = addr + 1;
      }
      CT = addr;
      objectFile.close();
    } catch (FileNotFoundException s) {
      CT = CB;
      System.err.println ("Error opening object file: " + s);
    } catch (IOException s) {
      CT = CB;
      System.err.println ("Error reading object file: " + s);
    }
  }


// RUNNING

  public static void main(String[] args) {
    System.out.println("********** TAM Interpreter (Java Version 2.1) HOLA MUNDO! **********");
    
    pointers.clear();
    
    for (int i = 0; i < data.length; i++) {
        data[i] = 0; //Clear memory
    }

    if (args.length == 1)
      objectName = args[0];
  	else
      objectName = "obj.tam";

    loadObjectProgram(objectName);
    if (CT != CB) {
      interpretProgram();
      showStatus();
    }
  }
  
  
}
