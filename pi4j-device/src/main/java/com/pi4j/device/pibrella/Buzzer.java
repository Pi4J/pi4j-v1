package com.pi4j.device.pibrella;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  Buzzer.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.gpio.GpioPinPwmOutput;

public class Buzzer {

    public static int STOP_FREQUENCY = 0;

    protected final GpioPinPwmOutput pwm;

    public Buzzer(GpioPinPwmOutput pwm){
        this.pwm = pwm;
    }

    public void buzz(int frequency){
        pwm.setPwm(frequency);
    }

    public void stop(){
        // stop the buzzer by setting the frequency to zero
        buzz(STOP_FREQUENCY);
    }

    // -------------------------------------------------------------------------------------
    // ATTENTION, THE BUZZER COMPONENT IS NOT YET COMPLETE
    // -------------------------------------------------------------------------------------

    /*
            # Play a single note, mathmatically
    # deduced from its index, offset from 440Hz
    def note(self,note):
    note = float(note)
    a = pow(2.0, 1.0/12.0)
    f = 440.00 * pow(a,note)
    self.buzz(f)
            return True

    # Example sound effects
    def success(self):
            # Repeat the last note to extend its duration
    self.melody([0,1,2,3,3,3,3,3],0.2,False)
            return True

    def fail(self):
            # Repeat the last note to extend its duration
    self.melody([5,4,3,2,1,1,1,1,1],0.2,False)
            return True

    def melody(self,notes,duration = 0.5,loop = True):
            self.stop()
    time_start = time.time()
    is_notation = False

    if notes[0] == 'N':
    is_notation = True
    notes.pop(0)

            if duration <= 0.0001:
    raise ValueError('Duration must be greater than 0.0001')

    if len(notes) == 0:
    raise ValueError('You must provide at least one note')

    # Get the total length of the tune
    # so we can play it!
    total = len(notes) * duration

    def melody():

    now = time.time() - time_start

    # Play only once if loop is false
            if loop == False and int(now / total) > 0:
            self._stop_buzzer()
            return False

    # Figure out how far we are into the current iteration
    # Then divide by duration to find the current note index
    delta = round( (now % total) / duration )

            # Select the note from the notes array
            note = notes[int(delta)-1]


            if is_notation:
            # this note and above would be OVER NINE THOUSAND Hz!
            # Treat it as an explicit pitch instead
    if note == 0:
            self._stop_buzzer()
            else:
            pibrella.buzzer.buzz(note)
            else:
            if note == '-':
            self._stop_buzzer()
            else:
            # Play the note
    pibrella.buzzer.note(note)

            # Sleep a bit
    time.sleep(0.0001)

    self._melody = AsyncWorker(melody)
    self.fps = 100
            self._melody.start()

    def alarm(self):

            # Play all notes from -30 to 30
            # with a note duration of 0.01sec
    # and, boom, we have an alarm!
            self.melody(range(-30,30),0.01)

    def notes(self,notation,speed=0.5):
            import re

    # Constant of about 1.0594
    N = pow( 2.0, (1.0/12.0) )

            # Table of notes, no support for flats YET
    note_key = ['A','A#','B','C','C#','D','D#','E','F','F#','G','G#']

            # Split our notation into individual notes
    notes = notation.split(' ')

            # print notes

    # Set up a list for our parsed output
    parsed = ['N']

            # Step through each note in turn
    for note in notes:

            # Split out the note and duration components
            detail = note.split(':')
    if len(detail) == 2:
            # We have a note and a duration
            note = detail[0]
    dur = int(detail[1])
            else:
            # We have just a note, so duration is 1 beat
            note = detail[0]
    dur = 1

            # Now try to match an octave
    octave = re.findall(r'\d+', note)

            # If we can't find one, default to octave 5
            if len(octave) == 0:
    octave = 5.0
            else:
    note = note.replace(octave[0],'')
    octave = float(octave[0])

            # If the note is a rest, turn off for that duration
    if note == 'R':
            for _ in range(dur):
            parsed.append(0) # Frequency of 0 ( off )
            else:
            # Otherwise, calculate the pitch of the note from A1 at 55Hz
    note_index = float(note_key.index( note ))

            # Pitch of the note itself is  1.0594 ^ note_index
            pitch = 55.000 * pow( N, note_index )

    # Then we shift up 2 to the power of the octave index -1
    pitch = round( pitch  * pow( 2, ( octave - 1 ) ) ,3)

            for _ in range(dur):
            parsed.append(pitch)

            self.melody(parsed,speed)

    def _stop_buzzer(self):
            self.duty_cycle(100)
            self.gpio_pwm.stop()

            time.sleep(0.02)

            GPIO.output(self.pin,0)

    def stop(self):
            if self._melody != None:
            self._melody.stop()

            self._stop_buzzer()

            return super(Buzzer,self).stop()
    */
}
