As one may expect, this is AntiMulti.

AntiMulti is a plugin which has undergone multiple changes from its original intent to be a simple plugin to prevent alternate accounts from being used on a server by blocking too many accounts from sharing an IP to a plugin which can also prevent session stealing using this same concept and also supporting password protection and admin protection. This is done simply by using an IP/Password combination to prevent the unauthorized use of an account by using IPs to prevent session stealing from working in the first place and using passwords as a backup in case a family member tries to use the account to destroy the server as well. All of this is completely toggle-able in the configuration file in case one does not use want to use certain features or not.

There are a few things you must understand when using this plugin. This is not a one-hit wonder in that it can stop all alternate accounts, as proxies would be able to get around this system, however common proxies would eventually be blocked after they are used enough. This has the un-intended benefit of also being able to hurt the PWNAGE client as well by building up the list of proxies it uses until eventually its collection of proxies have been blocked. This does its best though to stop a member from simply using 2 accounts to gain an advantage over another player.

There are 2 groups this plugin will understand: Players and Admins. Players are normal players and are not forced to use the protection system as strictly as admins. Players are more loosely held initially by allowing more connections per name and IP, however this can be changed. Admins are more protected to prevent the taking of the account to cause problems by forcing registration and using a smaller range of names and IPs that can those accounts can share. This is all permission based in that there is a perm that designates whether the player is an admin or a player, which is just antimulti.admin, which if given, marks the player as an admin and handles them accordingly.

This plugin has undergone a re-write where the plugin has been returned to a BETA state, so while most of the features are tested, there may be underlying bugs and issues which can cause undesirable actions. This has an update system which checks with the master server, located on Github for most reliable and up to date information, to make sure the version that is in use is up to date. This also checks to see the update priority of the latest update so that an admin can see if the developer has found a security issue and needs every one updated or just something small was patched that is minor to most servers.

As of this version, there are fewer commands. The commands for this version are:

- whitelist: This overrides the vanilla whitelist at the moment and uses a group whitelist instead. This means that when a group is on the whitelist for AntiMulti, they are allowed to connect to the server when this whitelist is active. This has the benefit of allowing this to be engaged whenever a griefer wave occurs to which when properly set up, can allow your more active members to still play while preventing the new players. This lets you have an easier switch by using permissions to determine if they are on this whitelist, which is antimulti.whitelist. The command's permission is antimulti.cmd.whitelist

- add: This lets you add players to ips so that they can connect even if they have reached their limit. This lets you allow players to have family connect to the server if the initial limit is lower than the accounts they have. This is done by using /add <name> <ip> which adds the name to that IP and vice versa. If the limit is not reached and this is used, this adds the name but will still count to the limits, so if the limit is set at 2 and you use the add command to add 1 IP, then there is still 1 IP free for the account. The permission for this command is antimulti.cmd.add

- register: This lets a player register a password for their account. This is used by /register <password> <password>. Once the password is created, the password has to be used to log in to the account, regardless of the settings in the config. This allows for players to create a personal password in case they have a family who uses their account without having to have the server require players to register. The permission for this defaults to true, with the permission node being antimulti.cmd.register

- login: This lets a player log in to the server after they have registered a password. Once a password is defined for the account, this has to be used to be able to play on the account after logging in to the server. This is done by using /login <password>. The permission for this defaults to all groups so this does not have to be defined unless your permissions plugin has a strict setting that requires permissions to be defined. The permission for this command is antimulti.cmd.login

- amreload: This lets the user reload the settings for AntiMulti. This is the preferred way to reload AntiMulti safely as using /reload can cause players not logged in to the server but who are already on to not have to use the password. A patch for this is not expected, so using /reload is NOT ADVISED with this plugin, however /amreload will reload AntiMulti safely. The permission for this is antimulti.cmd.reload

There are also other permission nodes as well that are not linked to commands but serve a purpose to the plugin as well.

- antimulti.cmd.*: This gives permission to use any of the AntiMulti commands. This should only be given to admins.

- antimulti.whitelist: This lets the group log in to the server when the AntiMulti whitelist is active. 

- antimulti.whitelist.notify: This lets the player know that the whitelist was toggled. This is good for admins to know if the whitelist was engaged or not and by who.

- antimulti.whitelist.*: This gives all the whitelist related permissions, which is antimulti.whitelist and antimulti.whitelist.notify. This does NOT give the permission to use the command however.

- antimulti.admin: This marks the group/user as an admin and to use the admin settings for the player instead of the player settings.

- antimulti.*: This gives every permission. Self-explanatory.

This plugin does log most things to log files located in the /plugins/AntiMulti/logs folder to allow owners to provide error logs to the developers when an error occurs. When an error occurs, we ask that you provide the entire log file that contains the error and what happened the error.

If you have any issues, please create a ticket on https://github.com/LordRalex/AntiMulti/issues with the log file and as much information as you can provide so the issue can be investigated and handled. If you have a question, feel free to ask http://dev.bukkit.org/server-mods/antimulti but please read everything first before you ask since your question may have already been answered elsewhere.